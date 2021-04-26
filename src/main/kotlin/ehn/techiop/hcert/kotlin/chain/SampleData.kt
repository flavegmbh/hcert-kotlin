package ehn.techiop.hcert.kotlin.chain

class SampleData {

    companion object {
        val recovery = """
        {
            "ver": "1.0.0",
            "nam": {
                "fn": "Musterfrau-G\u00f6\u00dfinger",
                "gn": "Gabriele",
                "fnt": "MUSTERFRAU<GOESSINGER",
                "gnt": "GABRIELE"
            },
            "dob": "1998-02-26",
            "r": [
                {
                    "tg": "840539006",
                    "fr": "2021-02-20",
                    "co": "AT",
                    "is": "BMGSPK Austria",
                    "df": "2021-04-04",
                    "du": "2021-10-04",
                    "ci": "ATOJSWGY3IOJUXGYTBOVWWC3TO"
                }
            ]
        }
        """.trimIndent()
        val vaccination = """
        {
            "ver": "1.0.0",
            "nam": {
                "fn": "Musterfrau-G\u00f6\u00dfinger",
                "gn": "Gabriele",
                "fnt": "MUSTERFRAU<GOESSINGER",
                "gnt": "GABRIELE"
            },
            "dob": "1998-02-26",
            "v": [
                {
                    "tg": "840539006",
                    "vp": "1119305005",
                    "mp": "EU\/1\/20\/1528",
                    "ma": "ORG-100030215",
                    "dn": 1,
                    "sd": 2,
                    "dt": "2021-02-18",
                    "co": "AT",
                    "is": "BMGSPK Austria",
                    "ci": "ATOZQWGY3IOJUXGYTBOVWWC3TO"
                }
            ]
        }
        """.trimIndent()
        val testRat = """
        {
            "ver": "1.0.0",
            "nam": {
                "fn": "Musterfrau-G\u00f6\u00dfinger",
                "gn": "Gabriele",
                "fnt": "MUSTERFRAU<GOESSINGER",
                "gnt": "GABRIELE"
            },
            "dob": "1998-02-26",
            "t": [
                {
                    "tg": "840539006",
                    "tt": "LP6464-4",
                    "tr": "260415000",
                    "ma": "1232",
                    "sc": "2021-02-20T12:34:56+00:00",
                    "dr": "2021-02-20T12:45:01+00:00",
                    "tc": "Testing center Vienna 1",
                    "co": "AT",
                    "is": "BMGSPK Austria",
                    "ci": "ATOJQXIY3IOJUXGYTBOVWWC3TO"
                }
            ]
        }
        """.trimIndent()
        val testNaa = """
        {
            "ver": "1.0.0",
            "nam": {
                "fn": "Musterfrau-G\u00f6\u00dfinger",
                "gn": "Gabriele",
                "fnt": "MUSTERFRAU<GOESSINGER",
                "gnt": "GABRIELE"
            },
            "dob": "1998-02-26",
            "t": [
                {
                    "tg": "840539006",
                    "tt": "LP6464-4",
                    "tr": "260415000",
                    "nm": "Roche LightCycler qPCR",
                    "sc": "2021-02-20T12:34:56+00:00",
                    "dr": "2021-02-20T14:56:01+00:00",
                    "tc": "Testing center Vienna 1",
                    "co": "AT",
                    "is": "BMGSPK Austria",
                    "ci": "ATNZQWCY3IOJUXGYTBOVWWC3TO"
                }
            ]
        }
        """.trimIndent()
    }

}
