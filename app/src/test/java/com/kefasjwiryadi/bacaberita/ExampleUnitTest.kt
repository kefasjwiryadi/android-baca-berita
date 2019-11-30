package com.kefasjwiryadi.bacaberita

import com.kefasjwiryadi.bacaberita.util.cleanContent
import com.kefasjwiryadi.bacaberita.util.toDateFormat
import com.kefasjwiryadi.bacaberita.util.toMillis
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    @Test
//    fun cleanUrlIsCorrect() {
//        val originalUrl =
//            "https://banjarmasin.tribunnews.com/2019/11/17/kesalahan-lina-disinggung-sule-tanggapi-isu-ibu-rizky-febian-ditinggal-suami-baru-saat-hamil-besar<entertainment"
//        val cleanedUrl =
//            "https://banjarmasin.tribunnews.com/2019/11/17/kesalahan-lina-disinggung-sule-tanggapi-isu-ibu-rizky-febian-ditinggal-suami-baru-saat-hamil-besar"
//        assertEquals(cleanedUrl, originalUrl.clearUrl())
//
//        val notFlaggedUrl =
//            "https://asdf.tribunnews.com/2019/11/17/kesalahan-lina-disinggung-sule-tanggapi-isu-ibu-rizky-febian-ditinggal-suami-baru-saat-hamil-besar"
//        assertEquals(notFlaggedUrl, notFlaggedUrl.clearUrl())
//    }

    @Test
    fun cleanContentIsCorrect() {
        val content =
            "JAKARTA, KOMPAS.com -\r\nMenteri Pendidikan dan Kebudayaan (Mendikbud) Nadiem Makarim mengungkapkan alasan yang melatarbelakangi rencana penghapusan ujian nasional (UN). \r\n Menurut dia, ada keinginan untuk menghindari dampak negatif dari UN tersebut.\r\n \"Banyak s… [+1517 chars]"
        val expected =
            "JAKARTA, KOMPAS.com - Menteri Pendidikan dan Kebudayaan (Mendikbud) Nadiem Makarim mengungkapkan alasan yang melatarbelakangi rencana penghapusan ujian nasional (UN). Menurut dia, ada keinginan untuk menghindari dampak negatif dari UN tersebut. \"Banyak s… [+1517 chars]"
        assertEquals(expected, content.cleanContent())
    }

    @Test
    fun dateTimeFormattingIsCorrect() {
        val isoDateTime = "2019-11-22T15:23:00Z"
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = isoDateTime.toMillis()
        assertEquals(2019, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.NOVEMBER, calendar.get(Calendar.MONTH))
        assertEquals(22, calendar.get(Calendar.DATE))
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(23, calendar.get(Calendar.MINUTE))
        assertEquals(0, calendar.get(Calendar.SECOND))

        assertEquals("22/11/2019 22:23 WIB", isoDateTime.toDateFormat())

    }

}
