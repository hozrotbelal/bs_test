package com.example.bs_test.utils

import android.provider.ContactsContract

const val ADD_MONEY_MIN = 50.0
const val ADD_MONEY_MAX = 30000.0
const val REQUEST_MONEY_MIN = 10.0
const val REQUEST_MONEY_MAX = 25000.0
var CHARGE_RATE: Float = .014f
var LOADING = 1
var DETAILS = 4
var DELETE = 1
var EXTRA_PX = 60
var SAVED_ACCOUNT_DETAILS = 3
var TAGGED_ACCOUNT_DETAILS = 2
var SUCCESS = 200
var LIMIT = 30
var PAGE_LIMIT = 50
var SEARCHETEXT = ""
var SONG_TIME: Long = 30000
var SEARCH_KEY: String = "android"
const val SPLASH_TIME: Long = 1000
const val COMPRESS_QUALITY = 40

var BEARER:String="Bearer "
var publishFlg:Int=1



const val STORY_DURATION:Long = 10000
const val HIGHLIGHT_DURATION:Long = 30000
const val SERVER_SEND_FILE_SIZE_MAX_LIMIT = 8000000
const val SERVER_SEND_FILE_SIZE_LIMIT = 700000
var defaultInterval: Int = 500
var SEARCH_TIME_DELAY: Long = 500
var MY_PAGE_HEIGHT = 400
var RefreshToken: String =
    "AQAJBvciq0Ubthg4_UijTtERGih_2ezk_XOxE9rVHI8tN49CwoPl5NBdgD4v-lsedq95899lKvlGnacNyzE4ZzwMVQvRSc1mwOIoI5ex_vjxtCoaXMYb7aqrWgDuiQcpkK0"
var BottomSheetHeight: Int = 600
var GrandType: String = "refresh_token"
var TRACK: String = "track"
var ACTION_LOGOUT: String = "logout"
var GB: String = "GB"
var HEADER_VALUE: String = "BEATS=6548083c00c45e88770bd236d20758d3"
var SPOTIFY_URL: String = "https://accounts.spotify.com/api/token"
var SPOTIFY_QUERY_URL: String = "https://api.spotify.com/v1/"
var SPOTIFY_BASIC_TOKEN: String =
    "Basic YjcxYThkMmJjMDI3NGE3MGEwY2NkZDExZjIyZjcwNjI6MTI3YzI5MDgxMmIzNDRhMWI4ZDUxNDU1ZGRmMjc1YTA="
var PROJECTION = arrayOf(
    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
    ContactsContract.Contacts.PHOTO_URI,
    ContactsContract.CommonDataKinds.Phone.NUMBER,
    ContactsContract.CommonDataKinds.Phone.CONTACT_ID

)

enum class ImeOptionStatus(val status: Int) {
    NO_INPUT(0),
    INVALID_INPUT(1),
    VALID_INPUT(2)
}

class PARAM {
    companion object {

        val TOKEN: String = "token"
        val TYPE: String = "type"

    }

}

const val FIRST_POETIC_WORD = 1
const val SECOND_POETIC_WORD = 2
const val HASHTAG_REQUEST_DATE_FORMAT = "yyyy-MM-dd"
const val HASHTAG_REQUEST_CREATED_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss"
const val DISCOVERY_ONE = 1
const val DISCOVERY_TWO = 2
const val DISCOVERY_THREE = 3

const val INTERVAL = 1000L
const val FASTEST_INTERVAL = 500L
const val LOCATION_PERMISSION_REQUEST = 101

var EMOTION_TAG:String="EMOTION_TAG"
var HASHTAG:String="HASH_TAG"

var EMOTION_TAG_JSON:String="{\n" +
        "    \"status\": 200,\n" +
        "    \"success\": true,\n" +
        "    \"message\": \"感情タグの一覧を取得しました。\",\n" +
        "    \"count\": 5,\n" +
        "    \"body\": {\n" +
        "        \"page\": \"1\",\n" +
        "        \"limit\": \"50\",\n" +
        "        \"current_total\": 5,\n" +
        "        \"data\": [\n" +
        "            {\n" +
        "                \"id\": 5,\n" +
        "                \"emotion_tag_content_ja\": \"怒り\",\n" +
        "                \"emotion_tag_content_en\": \"anger\",\n" +
        "                \"emotion_tag_content_zh\": \"\",\n" +
        "                \"emotion_tag_use_count\": 0,\n" +
        "                \"logical_del_flg\": 0,\n" +
        "                \"created\": \"2022/03/26 21:51:13\",\n" +
        "                \"updated\": \"2022/05/09 03:33:19\"\n" +
        "            },\n" +
        "            {\n" +
        "                \"id\": 4,\n" +
        "                \"emotion_tag_content_ja\": \"不安\",\n" +
        "                \"emotion_tag_content_en\": \"anxious\",\n" +
        "                \"emotion_tag_content_zh\": \"\",\n" +
        "                \"emotion_tag_use_count\": 0,\n" +
        "                \"logical_del_flg\": 0,\n" +
        "                \"created\": \"2022/03/26 21:51:13\",\n" +
        "                \"updated\": \"2022/05/09 03:33:04\"\n" +
        "            },\n" +
        "            {\n" +
        "                \"id\": 3,\n" +
        "                \"emotion_tag_content_ja\": \"イライラ\",\n" +
        "                \"emotion_tag_content_en\": \"frustration\",\n" +
        "                \"emotion_tag_content_zh\": \"\",\n" +
        "                \"emotion_tag_use_count\": 0,\n" +
        "                \"logical_del_flg\": 0,\n" +
        "                \"created\": \"2022/03/26 21:51:13\",\n" +
        "                \"updated\": \"2022/05/09 03:26:49\"\n" +
        "            },\n" +
        "            {\n" +
        "                \"id\": 2,\n" +
        "                \"emotion_tag_content_ja\": \"悲しみ\",\n" +
        "                \"emotion_tag_content_en\": \"saddness\",\n" +
        "                \"emotion_tag_content_zh\": \"\",\n" +
        "                \"emotion_tag_use_count\": 0,\n" +
        "                \"logical_del_flg\": 0,\n" +
        "                \"created\": \"2022/03/26 21:51:13\",\n" +
        "                \"updated\": \"2022/05/09 02:19:16\"\n" +
        "            },\n" +
        "            {\n" +
        "                \"id\": 1,\n" +
        "                \"emotion_tag_content_ja\": \"喜び\",\n" +
        "                \"emotion_tag_content_en\": \"joy\",\n" +
        "                \"emotion_tag_content_zh\": \"\",\n" +
        "                \"emotion_tag_use_count\": 0,\n" +
        "                \"logical_del_flg\": 0,\n" +
        "                \"created\": \"2022/03/26 21:51:13\",\n" +
        "                \"updated\": \"2022/05/09 02:09:25\"\n" +
        "            }\n" +
        "        ]\n" +
        "    }\n" +
        "}"

