package com.stockmarket.screener.excel;

import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PoiUtils {

    private static final LocalDate EXCEL_BASE_DATE = LocalDate.of(1899, 12, 30);

    public static String getStringValue(Cell cell) {
        if (cell == null) return null;
        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) {
            type = cell.getCachedFormulaResultType();
        }

        switch (type) {
            case STRING:
                String val = cell.getStringCellValue();
                if (val == null) return null;
                val = val.replace('\u00A0', ' ').replace('\uFEFF', ' ').trim();
                return val.isEmpty() ? null : val;

            case NUMERIC:
                try {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        if (date != null) {
                            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                        }
                    }
                } catch (Exception ignored) {}

                double num = cell.getNumericCellValue();
                if (isDateCell(cell, num)) {
                    try {
                        Date date = DateUtil.getJavaDate(num);
                        if (date != null) {
                            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                        }
                    } catch (Exception ignored) {}
                }

                if (num == (long) num) {
                    return String.valueOf((long) num);
                } else {
                    BigDecimal bd = BigDecimal.valueOf(num).setScale(2, RoundingMode.HALF_UP);
                    return bd.stripTrailingZeros().toPlainString();
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case BLANK:
            default:
                return null;
        }
    }

    public static BigDecimal getBigDecimalValue(Cell cell) {
        if (cell == null) return null;
        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) {
            type = cell.getCachedFormulaResultType();
        }

        switch (type) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP);

            case STRING:
                String text = cell.getStringCellValue();
                if (text == null || text.trim().isEmpty() || text.trim().equalsIgnoreCase("N/A") || text.trim().equals("-")) {
                    return null;
                }
                try {
                    String clean = text.replace('\u00A0', ' ').replace(",", "").replace("%", "").trim();
                    return new BigDecimal(clean).setScale(4, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    return null;
                }

            default:
                return null;
        }
    }

    public static LocalDate getDateValue(Cell cell) {
        if (cell == null) return null;
        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) {
            type = cell.getCachedFormulaResultType();
        }

        if (type == CellType.NUMERIC) {
            double num = cell.getNumericCellValue();
            if (num <= 1000) {
                return null;
            }

            try {
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        if (ld.getYear() >= 1990 && ld.getYear() <= 2050) {
                            return ld;
                        }
                    }
                }
            } catch (Exception ignored) {}

            if (num > 30000 && num < 70000) {
                try {
                    Date javaDate = DateUtil.getJavaDate(num);
                    if (javaDate != null) {
                        LocalDate ld = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        if (ld.getYear() >= 1990 && ld.getYear() <= 2050) {
                            return ld;
                        }
                    }
                } catch (Exception ignored) {}
                LocalDate ld = EXCEL_BASE_DATE.plusDays((long) num);
                if (ld.getYear() >= 1990 && ld.getYear() <= 2050) {
                    return ld;
                }
            }
        } else if (type == CellType.STRING) {
            String text = cell.getStringCellValue();
            if (text != null) {
                text = text.replace('\u00A0', ' ').trim();
                try {
                    LocalDate ld = LocalDate.parse(text);
                    if (ld.getYear() >= 1990 && ld.getYear() <= 2050) {
                        return ld;
                    }
                } catch (Exception ignored) {}
                try {
                    double num = Double.parseDouble(text);
                    if (num > 30000 && num < 70000) {
                        Date javaDate = DateUtil.getJavaDate(num);
                        if (javaDate != null) {
                            LocalDate ld = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            if (ld.getYear() >= 1990 && ld.getYear() <= 2050) {
                                return ld;
                            }
                        }
                        LocalDate ld = EXCEL_BASE_DATE.plusDays((long) num);
                        if (ld.getYear() >= 1990 && ld.getYear() <= 2050) {
                            return ld;
                        }
                    }
                } catch (Exception ignored) {}
            }
        }
        return null;
    }

    private static boolean isDateCell(Cell cell, double num) {
        if (num <= 30000 || num >= 70000) return false;
        try {
            if (DateUtil.isCellDateFormatted(cell)) return true;
        } catch (Exception ignored) {}
        try {
            Row r = cell.getRow();
            if (r != null) {
                if (r.getRowNum() == 2) return true; // Row 3 in Excel is date header in financial statement sheets
                Cell c0 = r.getCell(0);
                if (c0 != null) {
                    String lbl = getStringValue(c0);
                    if (lbl != null) {
                        String lower = lbl.trim().toLowerCase();
                        if (lower.contains("date") || lower.contains("narration") || lower.contains("year") || lower.contains("quarter")) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }
}
