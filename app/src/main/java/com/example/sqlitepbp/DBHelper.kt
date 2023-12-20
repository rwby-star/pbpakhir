import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "Login.db", null, 1) {

    data class warung(
        var idwarung: String,
        var namawarung: String,
        var logo: String,
        var gambar: String
    )

    companion object {
        const val DBNAME = "Login.db"
    }

    override fun onCreate(MyDB: SQLiteDatabase) {
        MyDB.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT)")
        MyDB.execSQL("CREATE TABLE warung(idwarung TEXT PRIMARY KEY, namawarung TEXT, logo TEXT, gambar TEXT)")
    }

    override fun onUpgrade(MyDB: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        MyDB.execSQL("DROP TABLE IF EXISTS users")
        MyDB.execSQL("DROP TABLE IF EXISTS warung")
    }

    fun insertData(username: String, password: String): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val result = MyDB.insert("users", null, contentValues)
        return result != -1L
    }

    fun insertWarung(idwarung: String, namawarung: String, logo: String, gambar: String): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("idwarung", idwarung)
        contentValues.put("namawarung", namawarung)
        contentValues.put("logo", logo)
        contentValues.put("gambar", gambar)
        val result = MyDB.insert("warung", null, contentValues)
        return result != -1L
    }

    fun viewWarung(): Cursor {
        val MyDB = this.writableDatabase
        return MyDB.rawQuery("SELECT * FROM warung", null)
    }

    fun getDataWarung(idwarung: String?): Cursor {
        val MyDB = this.writableDatabase
        return MyDB.rawQuery("SELECT * FROM warung WHERE idwarung = ?", arrayOf(idwarung))
    }

    fun updateWarung(idwarung: String, namawarung: String, logo: String, gambar: String): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("namawarung", namawarung)
        contentValues.put("logo", logo)
        contentValues.put("gambar", gambar)

        val whereClause = "idwarung = ?"
        val whereArgs = arrayOf(idwarung)

        val result = MyDB.update("warung", contentValues, whereClause, whereArgs)
        return result != -1
    }

    fun hapusWarung(id: String) {
        val db = this.writableDatabase

        db.delete("warung", "idwarung = ?", arrayOf(id))
    }

    fun checkUsername(username: String): Boolean {
        val MyDB = this.writableDatabase
        val cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ?", arrayOf(username))
        return cursor.count > 0
    }

    fun checkUsernamePassword(username: String, password: String): Boolean {
        val MyDB = this.writableDatabase
        val cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", arrayOf(username, password))
        return cursor.count > 0
    }
}
