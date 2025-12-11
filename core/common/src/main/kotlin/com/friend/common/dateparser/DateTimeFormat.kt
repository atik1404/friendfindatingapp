package com.friend.common.dateparser

import java.time.format.DateTimeFormatter

object DateTimePatterns {

    // --- SQL / API style (Y-M-D) ---

    /** 2025-12-05 */
    const val SQL_YMD = "yyyy-MM-dd"

    /** 2025-12-05 14:30 */
    const val SQL_YMD_HM = "yyyy-MM-dd HH:mm"

    /** 2025-12-05 14:30:45 */
    const val SQL_YMD_HMS = "yyyy-MM-dd HH:mm:ss"

    /** 2025-12-05 14:30:45+0600 */
    const val SQL_YMD_HMS_OFFSET_Z = "yyyy-MM-dd HH:mm:ssZ"

    /** 2025-12-05 14:30:45+06:00 */
    const val SQL_YMD_HMS_OFFSET_XXX = "yyyy-MM-dd HH:mm:ssXXX"


    // --- D-M-Y with dash ---

    /** 05-12-2025 */
    const val DMY_DASH = "dd-MM-yyyy"

    /** 05-12-2025 14:30 */
    const val DMY_DASH_HM = "dd-MM-yyyy HH:mm"

    /** 05-12-2025 14:30:45 */
    const val DMY_DASH_HMS = "dd-MM-yyyy HH:mm:ss"


    // --- D/M/Y with slash ---

    /** 05/12/2025 */
    const val DMY_SLASH = "dd/MM/yyyy"

    /** 05/12/2025 14:30 */
    const val DMY_SLASH_HM = "dd/MM/yyyy HH:mm"

    /** 05/12/2025 14:30:45 */
    const val DMY_SLASH_HMS = "dd/MM/yyyy HH:mm:ss"


    // --- With short month name (dd MMM yyyy) ---

    /** 05 Dec 2025 */
    const val DMY_TEXT = "dd MMM yyyy"

    /** 05 Dec 2025 14:30 */
    const val DMY_TEXT_HM = "dd MMM yyyy HH:mm"

    /** 05 Dec 2025 14:30:45 */
    const val DMY_TEXT_HMS = "dd MMM yyyy HH:mm:ss"


    // --- With short month name (MMM dd, yyyy) ---

    /** Dec 05, 2025 */
    const val MDY_TEXT_COMMA = "MMM dd, yyyy"

    /** Dec 05, 2025 14:30 */
    const val MDY_TEXT_COMMA_HM = "MMM dd, yyyy HH:mm"

    /** Dec 05, 2025 14:30:45 */
    const val MDY_TEXT_COMMA_HMS = "MMM dd, yyyy HH:mm:ss"


    // --- Time-only formats ---

    /** 14:30 (24h) */
    const val TIME_24_HM = "HH:mm"

    /** 14:30:45 (24h) */
    const val TIME_24_HMS = "HH:mm:ss"

    /** 02:30 PM (12h) */
    const val TIME_12_HM_AMPM = "hh:mm a"

    /** 02:30:45 PM (12h) */
    const val TIME_12_HMS_AMPM = "hh:mm:ss a"


    // --- ISO-like with 'T' and millis ---

    /** 2025-12-05T14:30:45 */
    const val ISO_YMD_THMS = "yyyy-MM-dd'T'HH:mm:ss"

    /** 2025-12-05T14:30:45.123 */
    const val ISO_YMD_THMS_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS"

    /** 2025-12-05T14:30:45.123Z */
    const val ISO_YMD_THMS_MILLIS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    /** 2025-12-05T14:30:45.123+06:00 */
    const val ISO_YMD_THMS_MILLIS_OFFSET_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    val inputFormatters = listOf(
        DateTimeFormatter.ISO_INSTANT,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,

        DateTimeFormatter.ofPattern(DateTimePatterns.SQL_YMD_HMS_OFFSET_Z),
        DateTimeFormatter.ofPattern(DateTimePatterns.SQL_YMD_HMS_OFFSET_XXX),

        DateTimeFormatter.ofPattern(DateTimePatterns.SQL_YMD_HMS),
        DateTimeFormatter.ofPattern(DateTimePatterns.SQL_YMD_HM),

        DateTimeFormatter.ofPattern(DateTimePatterns.DMY_DASH_HMS),
        DateTimeFormatter.ofPattern(DateTimePatterns.DMY_DASH_HM),

        DateTimeFormatter.ofPattern(DateTimePatterns.DMY_SLASH_HMS),
        DateTimeFormatter.ofPattern(DateTimePatterns.DMY_SLASH_HM),

        DateTimeFormatter.ofPattern(DateTimePatterns.DMY_TEXT_HMS),
        DateTimeFormatter.ofPattern(DateTimePatterns.DMY_TEXT_HM),
        DateTimeFormatter.ofPattern(DateTimePatterns.DMY_TEXT),

        DateTimeFormatter.ofPattern(DateTimePatterns.MDY_TEXT_COMMA_HM),
        DateTimeFormatter.ofPattern(DateTimePatterns.MDY_TEXT_COMMA)
    )
}



