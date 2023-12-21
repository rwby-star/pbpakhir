import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "Login.db", null, 1) {

//    data class warung(
//        var idwarung: String,
//        var namawarung: String,
//        var logo: String,
//        var gambar: String
//    )
//
//    data class Menu(
//        var idmenu: String,
//        var namamenu: String,
//        var hargamenu: String,
//        var kategorimenu: String,
//        var gambarmenu: String
//    )

//    companion object {
//        const val DBNAME = "Login.db"
//    }

    override fun onCreate(MyDB: SQLiteDatabase) {
        MyDB.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT)")
        MyDB.execSQL("CREATE TABLE warung(idwarung TEXT PRIMARY KEY, namawarung TEXT, logo TEXT, gambar TEXT)")
        MyDB.execSQL("CREATE TABLE menu(idmenu TEXT primary key, namamenu TEXT, hargamenu TEXT, gambarmenu TEXT, kategorimenu TEXT, idwarung TEXT, foreign key (idwarung) references warung(idwarung))")
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

    fun insertMenu(idmenu: String, namamenu: String, hargamenu: String, kategorimenu: String, gambarmenu: String, idwarung: String): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("idmenu", idmenu)
        contentValues.put("namamenu", namamenu)
        contentValues.put("kategorimenu", kategorimenu)
        contentValues.put("hargamenu", hargamenu)
        contentValues.put("gambarmenu", gambarmenu)
        contentValues.put("idwarung", idwarung)
        val result = MyDB.insert("menu", null, contentValues)
        return result != -1L
    }

    fun viewWarung(): Cursor {
        val MyDB = this.writableDatabase
        return MyDB.rawQuery("SELECT * FROM warung", null)
    }

    fun viewMenu(): Cursor {
        val MyDB = this.writableDatabase
        return MyDB.rawQuery("SELECT * FROM menu", null)
    }

    fun getDataWarung(idwarung: String?): Cursor {
        val MyDB = this.writableDatabase
        return MyDB.rawQuery("SELECT * FROM warung WHERE idwarung = ?", arrayOf(idwarung))
    }

    fun getDataMenu(idMenu: String?): Cursor {
        val MyDB = this.writableDatabase
        return MyDB.rawQuery("SELECT * FROM menu WHERE idmenu = ?", arrayOf(idMenu))
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

    fun updateMenu(idMenu: String, namaMenu: String, hargaMenu: String, kategoriMenu: String, gambarMenu: String): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("namamenu", namaMenu)
        contentValues.put("hargamenu",hargaMenu )
        contentValues.put("kategorimenu", kategoriMenu)
        contentValues.put("gambarmenu", gambarMenu)

        val whereClause = "idmenu = ?"
        val whereArgs = arrayOf(idMenu)

        val result = MyDB.update("menu", contentValues, whereClause, whereArgs)
        return result != -1
    }

    fun getWarungLogoById(idWarung: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT logo FROM warung WHERE idwarung = ?", arrayOf(idWarung))
        var logo: String? = null

        if (cursor.moveToFirst()) {
            logo = cursor.getString(cursor.getColumnIndex("logo"))
        }

        cursor.close()
        return logo
    }

    fun getWarungGambarById(idWarung: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT gambar FROM warung WHERE idwarung = ?", arrayOf(idWarung))
        var gambar: String? = null

        if (cursor.moveToFirst()) {
            gambar = cursor.getString(cursor.getColumnIndex("gambar"))
        }

        cursor.close()
        return gambar
    }

    fun getMenuGambarById(idMenu: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT gambarmenu FROM menu WHERE idmenu = ?", arrayOf(idMenu))
        var gambarmenu: String? = null

        if (cursor.moveToFirst()) {
            gambarmenu = cursor.getString(cursor.getColumnIndex("gambarmenu"))
        }

        cursor.close()
        return gambarmenu
    }

    fun hapusWarung(id: String) {
        val db = this.writableDatabase

        db.delete("warung", "idwarung = ?", arrayOf(id))
    }

    fun hapusMenu(id: String) {
        val db = this.writableDatabase

        db.delete("menu", "idmenu = ?", arrayOf(id))
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

    fun getIdWarung(): List<String> {
        val categoriesList = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT DISTINCT idwarung FROM warung" // Ganti your_table_name dengan nama tabel yang sesuai
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val idwarung = cursor.getString(cursor.getColumnIndex("idwarung"))
                categoriesList.add(idwarung)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categoriesList
    }
}
