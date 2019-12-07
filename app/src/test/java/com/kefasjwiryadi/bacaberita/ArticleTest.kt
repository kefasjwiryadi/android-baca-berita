package com.kefasjwiryadi.bacaberita

import com.kefasjwiryadi.bacaberita.util.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ArticleTest {

    @Test
    fun testCleanContent() {
        val content =
            "JAKARTA, KOMPAS.com -\r\nMenteri Pendidikan dan Kebudayaan (Mendikbud) Nadiem Makarim mengungkapkan alasan yang melatarbelakangi rencana penghapusan ujian nasional (UN). \r\n Menurut dia, ada keinginan untuk menghindari dampak negatif dari UN tersebut.\r\n \"Banyak s… [+1517 chars]"
        val expected =
            "JAKARTA, KOMPAS.com - Menteri Pendidikan dan Kebudayaan (Mendikbud) Nadiem Makarim mengungkapkan alasan yang melatarbelakangi rencana penghapusan ujian nasional (UN). Menurut dia, ada keinginan untuk menghindari dampak negatif dari UN tersebut. \"Banyak s… [+1517 chars]"
        assertEquals(expected, content.cleanContent())
    }

    @Test
    fun testCleanTitle() {
        val titles = listOf<Pair<String, String>>(
            "Awas, Penyakit Paru Langka Akibat Vape - Gaya - Tempo" to "Awas, Penyakit Paru Langka Akibat Vape - Gaya",
            "Raja Salman: Saudi Marah dengan Penembakan Biadab di Pangkalan... - SINDOnews.com" to "Raja Salman: Saudi Marah dengan Penembakan Biadab di Pangkalan...",
            "Cara Baru Astronom Melacak Kehidupan di Planet Lain | Teknologi - Gatra" to "Cara Baru Astronom Melacak Kehidupan di Planet Lain | Teknologi",
            "Diduga Jadi Selir Dirut Garuda, Berikut 5 Fakta Puteri Novitasari Ramli sang Pramugari Cantik yang Disebut ... - Grid.ID" to "Diduga Jadi Selir Dirut Garuda, Berikut 5 Fakta Puteri Novitasari Ramli sang Pramugari Cantik yang Disebut ...",
            "Melly Mono Gantikan Tantri Jadi Vokalis Band Kotak - VIVA - VIVA.co.id" to "Melly Mono Gantikan Tantri Jadi Vokalis Band Kotak",
            "Vicky Prasetyo Jadi Tersangka Pencemaran Nama Baik, Ini Kata Angel Lelga - Kompas.com - KOMPAS.com" to "Vicky Prasetyo Jadi Tersangka Pencemaran Nama Baik, Ini Kata Angel Lelga"
        )

        for (titlePair in titles) {
            assertEquals(titlePair.second, titlePair.first.cleanTitle())
        }
    }

    @Test
    fun testDateFormatting() {
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

    @Test
    fun testGetContent1() {
        val html = getStringFromFile("html1.html")
        val partialContent =
            "JAKARTA, KOMPAS.com - Penyanyi campursari Didi Kempot menggelar konser di The Pallas, SCBD, Jakarta Selatan, Jumat (6/12/2019) yang bertajuk \" The Lord of Loro Ati\".\r\nMeski The Pallas dikenal sebagai tempat hiburan anak muda dengan desain yang mewah dan masa …"
        val expected =
            "<p><strong>JAKARTA, KOMPAS.com</strong> - Penyanyi campursari <a target=\"_self\" class=\"inner-link-tag\" href=\"https://www.kompas.com/tag/Didi-Kempot\" style=\"color:#428bca;\">Didi Kempot</a> menggelar konser di The Pallas, SCBD, Jakarta Selatan, Jumat (6/12/2019) yang bertajuk \" <a target=\"_self\" class=\"inner-link-tag\" href=\"https://www.kompas.com/tag/The-Lord-of-Loro-Ati\" style=\"color:#428bca;\">The Lord of Loro Ati</a>\".</p>\n" +
                    "<p>Meski The Pallas dikenal sebagai tempat hiburan anak muda dengan desain yang mewah dan masa kini, khusus untuk Didi Kempot dan para Sobat Ambyar tempat itu disulap bak panggung campursari.</p>\n" +
                    "<p>Ya, ini merupakan kali pertama musik campursari masuk The Pallas.</p>\n" +
                    "<p><strong>Baca juga: <a href=\"https://www.kompas.com/hype/read/2019/12/03/134146766/dory-harsa-penabuh-kendang-didi-kempot-yang-digilai-para-sad-girl\" class=\"inner-link-baca-juga\" target=\"_self\">Dory Harsa, Penabuh Kendang Didi Kempot yang Digilai Para Sad Girl</a></strong></p>\n" +
                    "<p>Meski terkesan tradisional, terlihat para Sobat Ambyar datang mengenakan pakaian model kekinian.</p>\n" +
                    "<p>Tampak juga mereka menikmati tembang-tembang sang Godfather of Broken Heart dengan berjoget berasama.</p>\n" +
                    "<p>Hal itu terlihat ketika Didi Kempot membawakan lagu hits \"Stasiun Balapan\".</p>\n" +
                    "<p><strong>Baca juga: <a href=\"https://www.kompas.com/hype/read/2019/12/06/171233066/ambyar-didi-kempot-jadi-brand-ambassador-terbaru-shopee-indonesia\" class=\"inner-link-baca-juga\" target=\"_self\">Ambyar! Didi Kempot Jadi Brand Ambassador Terbaru Shopee Indonesia</a></strong></p>\n" +
                    "<p>\"<em>Ning stasiun balapan//Kuto solo sing dadi kenangan//Kowe karo aku//Naliko ngeterke lungamu</em>,\" begitulah lirik yang dilantunkan Didi Kempot.</p>\n" +
                    "<p>Irama tabuhan gendang bertempo cepat yang mengiringi nyanyian Didi Kempot menambah asyik suasana.</p>\n" +
                    "<p>Tidak berselang lama, pria yang bernama lengkap Dionisius Prasetyo itu langsung melantunkan \"Kalung Emas\".</p>\n" +
                    "<p><strong>Baca juga: <a href=\"https://www.kompas.com/hype/read/2019/12/06/191817566/didi-kempot-gelar-the-lord-of-loro-ati-antrean-mengular-sejak-sore\" class=\"inner-link-baca-juga\" target=\"_self\">Didi Kempot Gelar The Lord of Loro Ati, Antrean Mengular Sejak Sore</a></strong></p>\n" +
                    "<p>Di tengah-tengah lagu, Didi Kempot mengajak Sobat Ambyar bergoyang.</p>\n" +
                    "<p>\"Ayo di lagu ini semua goyang tipis-tipis,\" sorak Didi.</p>\n" +
                    "<p>Kalimat yang keluar tersebut semakin membakar semangat ratusan penonton untuk berdendang.</p>\n" +
                    "<img src=\"https://cdn1.tstatic.net/img/logo/tribun/svg/tribunstylecom.svg\" alt=\"TribunStyle.com\" border=\"0\" height=\"40\" style=\"height:40px;width:auto\">\n" +
                    "<p><strong>Baca juga: <a href=\"https://www.kompas.com/hype/read/2019/12/06/215550966/buka-konser-the-lord-of-loro-ati-didi-kempot-jakarta-emang-ambyar\" class=\"inner-link-baca-juga\" target=\"_self\">Buka Konser The Lord of Loro Ati, Didi Kempot: Jakarta Emang Ambyar!</a></strong></p>\n"
        assertEquals(
            expected,
            html.getContent(partialContent.cleanContent())
        )
    }

    @Test
    fun testGetContent2() {
        val html = getStringFromFile("html2.html")
        val partialContent =
            "Jakarta -\r\n Setelah sebulan lebih jabatan strategis di tubuh Polri kosong, seorang jenderal bintang dua terpilih mengisinya. Bukan sembarang orang, dia merupakan mantan ajudan Presiden Joko Widodo (Jokowi).Dialah Inspektur Jenderal (Irjen) Listyo Sigit Prabow…"
        val expected = "<div class=\"detail_text\"> \n" +
                " <!-- s:pic detail --> \n" +
                " <!-- S:read image orientation if potrait load this --> \n" +
                " <!-- E:read image orientation if potrait load this --> \n" +
                " <!-- e:pic detail --> \n" +
                " <b>Jakarta</b> - Setelah sebulan lebih jabatan strategis di tubuh \n" +
                " <a href=\"https://www.detik.com/tag/polri\">Polri</a> kosong, seorang jenderal bintang dua terpilih mengisinya. Bukan sembarang orang, dia merupakan mantan ajudan Presiden Joko Widodo (\n" +
                " <a href=\"https://www.detik.com/tag/jokowi\">Jokowi</a>).\n" +
                " <br>\n" +
                " <br>Dialah Inspektur Jenderal (Irjen) Listyo Sigit Prabowo. Dia akan mengemban amanah memimpin Badan Reserse Kriminal Polri sebagai Kabareskrim yang ditinggalkan Jenderal Idham Azis sejak 1 November 2019 karena menjadi Kapolri.\n" +
                " <br>\n" +
                " <br>Kelak setelah resmi dilantik sebagai Kabareskrim, bintang di bahu Listyo akan bertambah satu dengan pangkat Komisaris Jenderal (Komjen). Lantas bagaimana sepak terjang Listyo selama ini?\n" +
                " <br>\n" +
                " <br>\n" +
                " <!--s:parallaxindetail-->   \n" +
                " <!--e:parallaxindetail-->Merunut dari tahun 2011, Listyo Sigit pernah menjabat sebagai Kapolres Solo. Dia menyandang pangkat Komisaris Besar Polisi saat itu. Listyo Sigit saat menjabat Kapolres Solo pernah menangani kasus bom bunuh diri di Gereja Bethel Injil Sepenuh (GBIS), Kepunton, Solo, Jawa Tengah.\n" +
                " <br>\n" +
                " <br>\n" +
                " <br>\n" +
                " <br>\n" +
                " <br>Loncat ke tahun 2014, Listyo Sigit dipercaya menjadi ajudan Presiden Jokowi yang saat itu memerintah bersama dengan Jusuf Kalla. Kombes Listyo Sigit mulai bertugas menjadi ajudan Jokowi dari kalangan polisi pada Senin, 27 Oktober 2014.\n" +
                " <br>\n" +
                " <br>Dua tahun berselang, Listyo Sigit kembali mendapat promosi. Dengan pangkat Brigadir Jenderal Polisi, Listyo Sigit dipercaya menduduki posisi Kapolda Banten. Sebagai Kapolda Banten, Listyo Sigit sempat mengamankan Pilgub Banten 2017 dan Pilkada serentak 2018. Listyo Sigit bertugas sekitar 2 tahunan sebagai Kapolda Banten.\n" +
                " <br>\n" +
                " <br>Pada 2018, Kapolda Banten Brigjen Listyo Sigit Prabowo menjadi salah satu anggota yang mendapatkan promosi jabatan di lingkungan Polri. Dia diangkat menjadi Kadiv Propam menggantikan Irjen Martuani Sormin, yang ditugaskan sebagai Kapolda Papua. Dia juga mendapatkan kenaikan pangkat menjadi Inspektur Jenderal Polisi.\n" +
                " <br>\n" +
                " <br>  \n" +
                " <!-- E:newstag --> \n" +
                " <!-- s:multi-nav -->  \n" +
                " <!-- e:multi-nav --> \n" +
                "</div>"
        assertEquals(expected.trim(), html.getContent(partialContent.cleanContent()).trim())
    }

    @Test
    fun testGetContent3() {
        val html = getStringFromFile("html3.html")
        val partialContent =
            "VIVA  Ajang penghargaan bergengsi Panasonic Gobel Awards, baru saja digelar meriah pada Jumat malam, 6 Desember 2019 di The Tribrata Dharmawangsa, Jakarta Selatan. Sebanyak 24 kategori telah dibacakan pemenangnya.\\r\\nBeda dari tahun sebelumnya, ada kategori Kre… [+2001 chars]"
        val expected =
            "<p><strong>VIVA</strong> – Ajang penghargaan bergengsi Panasonic Gobel Awards, baru saja digelar meriah pada Jumat malam, 6 Desember 2019 di The Tribrata Dharmawangsa, Jakarta Selatan. Sebanyak 24 kategori telah dibacakan pemenangnya.</p>\n" +
                    "<p>Beda dari tahun sebelumnya, ada kategori Kreator Konten Digital pada tahun ini. Tak hanya itu, almarhum dua tokoh pertelevisian Indonesia turut mendapatkan penghargaan.</p>\n" +
                    "<p>Adalah Ali Shahab, yang mendapatkan Anugerah Legend Award atas kepeloporannya di dunia sinetron. Kemudian, Arswendo Atmowiloto dianugerahi Lifetime Achievement atas jasa-jasanya berkarya hingga akhir hayat.</p>\n" +
                    "<p>Berikut merupakan daftar lengkap pemenang Panasonic Gobel Awards 2019 :</p>\n" +
                    "<p><strong>1. Program Sinetron Serial Terfavorit</strong></p>\n" +
                    "<p>Tukang Ojeg Pengkolan - RCTI</p>\n" +
                    "<p><strong>2. Program Sinetron Nonserial Terfavorit</strong></p>\n" +
                    "<p>FTV Siang - SCTV</p>\n" +
                    "<p><strong>3. Program Berita Terfavorit</strong></p>\n" +
                    "<p>Seputar iNews Siang - RCTI</p>\n" +
                    "<p><strong>4. Program Berita Talkshow Terfavorit</strong></p>\n" +
                    "<p>Mata Najwa - Trans 7</p>\n" +
                    "<p><strong>5. Program Kuis dan Game Show Terfavorit</strong></p>\n" +
                    "<p>Super Deal Indonesia - GTV</p>\n" +
                    "<p><strong>6. Program Reality Show Terfavorit</strong></p>\n" +
                    "<p>Bedah Rumah - GTV</p>\n" +
                    "<p><strong>7. Program Musik Terfavorit</strong></p>\n" +
                    "<p>Breakout - Net TV</p>\n" +
                    "<p><strong>8. Program Pencarian Bakat Terfavorit</strong></p>\n" +
                    "<p>Master Chef Indonesia - RCTI</p>\n" +
                    "<p><strong>9. Program Anak Terfavorit</strong></p>\n" +
                    "<p>Kiko - MNC</p>\n" +
                    "<p><strong>10. Program Entertainment Variety &amp; Talkshow Terfavorit</strong></p>\n" +
                    "<p>Tonight Show - Net TV</p>\n" +
                    "<p><strong>11. Program Kompetisi Olahraga Terfavorit</strong></p>\n" +
                    "<p>Shopee Liga 1 - Indosiar</p>\n" +
                    "<p><strong>12. Program Acara Khusus Terfavorit</strong></p>\n" +
                    "<p>Indonesian Television Awards 2018</p>\n" +
                    "<p><strong>13. Pemeran Pria Sinetron Terfavorit</strong></p>\n" +
                    "<p>Ammar Zoni</p>\n" +
                    "<p><strong>14. Pemeran Wanita Sinetron Terfavorit</strong></p>\n" +
                    "<p>Irish Bella</p>\n" +
                    "<p><strong>15. Presenter Berita Terfavorit</strong></p>\n" +
                    "<p>Tommy Tjokro - Seputar iNews Siang (RCTI)</p>\n" +
                    "<p><strong>16. Presenter Berita Talkshow Terfavorit</strong></p>\n" +
                    "<p>Najwa Shihab - Mata Najwa (Trans 7)</p>\n" +
                    "<p><strong>17. Presenter Entertainment Variety &amp; Talkshow Terfavorit</strong></p>\n" +
                    "<p>Ruben Onsu - Brownies (Trans TV)</p>\n" +
                    "<p><strong>18. Presenter Kuis dan Game Show Terfavorit</strong></p>\n" +
                    "<p>Raffi Ahmad - Indonesia Pintar (SCTV)</p>\n" +
                    "<p><strong>19. Presenter Majalah &amp; Pertandingan Olahraga Terfavorit</strong></p>\n" +
                    "<p>Valentino Jebreet</p>\n" +
                    "<p><strong>20. Presenter Pencarian Bakat Terfavorit</strong></p>\n" +
                    "<p>Ananda Omesh - The Voice Indonesia (GTV)</p>\n" +
                    "<p><strong>21. Kreator Konter Digital Terfavorit</strong></p>\n" +
                    "<p>RANS Entertainment</p>\n" +
                    "<p><strong>22. Stasiun Televisi Terfavorit di Media Sosial</strong></p>\n" +
                    "<p>MNC TV</p>\n" +
                    "<p><strong>23. Web Series Terfavorit</strong></p>\n" +
                    "<p>Rompis - Klaklik</p>\n" +
                    "<p><strong>24. Web Series Brand Terfavorit</strong></p>\n" +
                    "<p>Milenial \"Males\" jadi Miliuner - Smartfren</p>\n"
        assertEquals(expected, html.getContent(partialContent.cleanContent()))
    }

    private fun getStringFromFile(filename: String): String {
        var scanner: Scanner? = null
        try {
            scanner = Scanner(File("./src/test/java/com/kefasjwiryadi/bacaberita/$filename"))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val newsContentHtml = scanner!!.useDelimiter("\\A").next()
        scanner.close() // Put this call in a finally block
        return newsContentHtml
    }

}
