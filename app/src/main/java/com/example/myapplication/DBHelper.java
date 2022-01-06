package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public static final int DB_VERSION = 1;
    //버전을 높이면 onUpgrade()가 호출된다고 하네요. 테이블을 지우고 onCreate를 하기 위함.
    public DBHelper(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {

        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, name TEXT)");
        MyDB.execSQL("CREATE TABLE bookinfo (name char(20), artist char(10), info char(200), image blob);");
//        MyDB.execSQL("CREATE TABLE bookcontents();");
        MyDB.execSQL("INSERT INTO bookinfo VALUES ('Little Red Riding Hood','Beatrix Potter', 'information', NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists bookinfo");
        onCreate(MyDB);
    }

    public Boolean insertData(String username, String password, String name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("name", name);
        long result = MyDB.insert("users", null, contentValues);
        if(result == -1) return false;
        else
            return true;
    }

    public Boolean checkusername(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username=? and password = ?", new String[] {username, password});
        if(cursor.getCount() >0)
            return true;
        else
            return false;
    }
    
    // 모든 책에 대한 제목과 url를 가져오게함 => 추후에 특정 책에 대한 정보만을 가져오도록 수정
    public ArrayList<String[]> getBookInfo(){

        ArrayList<String[]> book_info = new ArrayList<String[]>();
        SQLiteDatabase MyDB = this.getWritableDatabase();
        //String[] str = new String[3];
        Cursor cursor = MyDB.rawQuery("SELECT * FROM bookinfo;", null);

        while (cursor.moveToNext()) {
            String[] str = new String[2];
            // book_title
            str[0] = cursor.getString(0);
            // book_url => Unable to convert BLOB to string
            str[1] = cursor.getString(3);

            // add가 되지않음
            book_info.add(str);
        }

        return book_info;
    }

    public String[] getBookInfo2(String book_title){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        String[] str = new String[4];

        // 하나의 book_title의 book_info 모든 정보 가져오기
        Cursor cursor = MyDB.rawQuery("SELECT * FROM bookinfo where name=?;", new String[] {book_title});

        while(cursor.moveToNext()){
            str[0] = cursor.getString(0);
            str[1] = cursor.getString(1);
            str[2] = cursor.getString(2);
            str[3] = cursor.getString(3);
        }
        return str;
    }
// 그린 그림 가져오기
    public ArrayList<Integer> get_user_book(String username, String bookname){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ArrayList<Integer> page = new ArrayList<Integer>();
        Cursor cursor = MyDB.rawQuery("SELECT  * FROM user_book where Username = ? and book_title =?;", new String[]{username,bookname});

        while (cursor.moveToNext()) {
            page.add(cursor.getInt(2));
        }
        return page;
    }

    public Bitmap getuserimage(String username, String bookname){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ArrayList<Integer> page = new ArrayList<Integer>();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM user_book where Username = ? and id = 1;", new String[]{username});
        byte[] bitmap = cursor.getBlob(3);

        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        return image;
    }

    public Bitmap getImage(String name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from bookinfo where name=?", new String[] {name});
        cursor.moveToFirst();
        byte[] bitmap = cursor.getBlob(3);
        if(cursor.getBlob(3) == null)
            return null;
        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        return image;
    }

    //그린 페이지 수
    public int maxpage( String bookname){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        int page =0;
        Cursor cursor = MyDB.rawQuery("SELECT count(id) FROM user_book where book_title =?;", new String[]{bookname});
        while (cursor.moveToNext()) {
            page = cursor.getInt(0);
        }
        return page;
    }


    // book_contents에서 contents 얻기 => 특정 타이틀에 특정 id에 대하여 전달 받음
    public String[] getBookContents(String title , int id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        // Cursor 선택된 행의 집합 객체 정도
        Cursor cursor = MyDB.rawQuery("SELECT * FROM book_contents where book_title=? and id=?", new String[]{title, String.valueOf(id)});
        System.out.println(cursor);
        try {
            cursor.moveToFirst();
            String contents = cursor.getString(1);
            String content2 = cursor.getString(4);
            //System.out.println(contents);
            return new String[]{contents, content2};
        }finally {
            cursor.close();
        }
    }

    // book_contents에서 contents 애러채크. content두번째에 null이 있는지 확인함.
    public int getBookContentCheck(String title , int id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        // Cursor 선택된 행의 집합 객체 정도
        Cursor cursor = MyDB.rawQuery("SELECT * FROM book_contents where book_title=? and id=?", new String[]{title, String.valueOf(id)});

        try{
            cursor.moveToFirst();
            if(cursor.moveToFirst()) {
                cursor.close();
                return 1; // 있다
            }
            else{
                cursor.close();
                return 0;
            }
        }
        catch (Exception e){
            cursor.close();
            return 0; // 없다.
        }
    }

    // book_contents에서  book image url get
    public String[] getBookUrl(String title, int id){
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("SELECT * FROM book_contents where book_title=? and id=?", new String[]{title, String.valueOf(id)});
        cursor.moveToFirst();
        String url1 = cursor.getString(3);
        String url2 = cursor.getString(5);
        return new String[] {url1, url2};
    }

    public String[] getBookContentsUrl(String title, int id){
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("SELECT * FROM book_contents where book_title=? and id=?", new String[]{title, String.valueOf(id)});
        try {
            cursor.moveToFirst();

            String url2 = cursor.getString(5);
            return new String[]{url2};
        }finally {
            cursor.close();
        }

    }

    public int getMaxPage(String title){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        // Cursor 선택된 행의 집합 객체 정도
        Cursor cursor = MyDB.rawQuery("SELECT * FROM book_contents where  book_title=?", new String[]{title});
        cursor.moveToLast();
        // 0번째 column: page 반환
        //System.out.println(cursor.getInt(0));
        return cursor.getInt(0);
    }
// 사용자가 그린 그림 페잊 수
    public int getMaxPage2(String title, String name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        // Cursor 선택된 행의 집합 객체 정도
        Cursor cursor = MyDB.rawQuery("SELECT * FROM user_book where  book_title=? and Username =?", new String[]{title,name});
        cursor.moveToLast();
        // 0번째 column: page 반환
        //System.out.println(cursor.getInt(0));
        return cursor.getInt(2);
    }

    // bookinfo에 image 넣기
    public boolean insertImage(String username, byte[] img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", username);
        contentValues.put("image", img);
        long result = MyDB.update("bookinfo",contentValues, "name=?", new String[]{username});
        if(result == -1) return false;
        else return true;
    }
   /* // user_book에 image 넣기
    public boolean insertEditImage(String username, byte[] img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", username);
        contentValues.put("image", img);
        long result = MyDB.update("bookinfo",contentValues, "name=?", new String[]{username});
        if(result == -1) return false;
        else return true;
    }*/


    // book_contents에 image 넣기: 사용자 이미지 넣기
    public boolean insertImageToContents(byte[] img, String book_title, int id){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        System.out.println("db image");
        System.out.println(img);

        contentValues.put("image2", img);
        long result = MyDB.update("book_contents", contentValues, "id=? and book_title=? ", new String[]{String.valueOf(id), book_title});
        if(result == -1) return false;
        else return true;
    }

    public boolean insertuserImage(String username, String bookname, int page, byte[] img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", username);
        contentValues.put("book_title", bookname);
        contentValues.put("id",page);
        contentValues.put("image", img);
        long result = MyDB.insert("user_book",null,contentValues);
        if(result == -1) return false;
        else return true;
    }
    // 이미 이미지가 있을때는 PK 중복 피하기 위해서 이미지만 찾아서 update해주기
    public boolean insertuserImage2(String username, String bookname, int page, byte[] img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("image", img);
        long result = MyDB.update("user_book",  contentValues,
                "username = ? and book_title = ? and id = ?",
                new String[]{username, bookname, String.valueOf(page)});
        if(result == -1) return false;
        else return true;
    }

    // 테이블에서 해당 이미지 있는가 채크
    public int check(String username, String title, int id){
        SQLiteDatabase MyDBUser = this.getWritableDatabase();
        Cursor cursorUser = MyDBUser.rawQuery(
                "Select * from user_book " +
                        "where book_title=? " +
                        "and id=? " +
                        "and username=?",
                new String[] {title,String.valueOf(id),username});
        try{
            cursorUser.moveToFirst();
            if(cursorUser.moveToFirst()) {
                cursorUser.close();
                return 1; // 있다
            }
            else{
                cursorUser.close();
                return 0;
            }
        }
        catch (Exception e){
            cursorUser.close();
            return 0; // 없다.
        }
    }

    /*// 테이블에서 수정할 이미지 가져오기
    public Bitmap getEditImage(String username, String title, int id){
        SQLiteDatabase MyDBUser = this.getWritableDatabase();
        Cursor cursorUser = MyDBUser.rawQuery(
                "Select * from user_book " +
                        "where book_title=? " +
                        "and id=? " +
                        "and username=?",
                new String[] {title,String.valueOf(id),username});
        cursorUser.moveToFirst();
        if(cursorUser.moveToFirst()){   // 해당 커서가 (data)가 있다면
            byte[] bitmapUser = cursorUser.getBlob(3);
            Bitmap imageUser = BitmapFactory.decodeByteArray(bitmapUser, 0, bitmapUser.length);
            return imageUser;
        }
        else{   // 없으면 기본 수정 이미지 주기
            SQLiteDatabase MyDB = this.getWritableDatabase();
            Cursor cursor = MyDB.rawQuery("Select * from book_contents where book_title=? and id=?", new String[] {title,String.valueOf(id)});
            cursor.moveToFirst();
            byte[] bitmap = cursor.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            return image;
        }
    }*/

    // 테이블에서 수정전 이미지 가져오기
/*    public Bitmap getImageBasic(String title, int id){
            SQLiteDatabase MyDB = this.getWritableDatabase();
            Cursor cursor = MyDB.rawQuery("Select * from book_contents where book_title=? and id=?", new String[] {title,String.valueOf(id)});
            try{
                cursor.moveToFirst();
                byte[] bitmap = cursor.getBlob(5);
                Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
                return image;
            }
            finally {
                cursor.close();
            }

    }*/

    // 테이블에서 수정한 이미지 가져오기
    public Bitmap getImageEdited(String username, String title, int id){
        SQLiteDatabase MyDBUser = this.getWritableDatabase();
        Cursor cursorUser = MyDBUser.rawQuery(
                "Select * from user_book " +
                        "where book_title=? " +
                        "and id=? " +
                        "and username=?",
                new String[] {title,String.valueOf(id),username});
        try {
            cursorUser.moveToFirst();
            byte[] bitmapUser = cursorUser.getBlob(3);
            Bitmap imageUser = BitmapFactory.decodeByteArray(bitmapUser, 0, bitmapUser.length);
            return imageUser;
        }
        finally {
            cursorUser.close();
        }
    }

// 사용자가 그린 이미지 불러오기
    public Bitmap getImageEdited2(String username, String title, int id){
        SQLiteDatabase MyDBUser = this.getWritableDatabase();
        Cursor cursorUser = MyDBUser.rawQuery(
                "Select * from user_book " +
                        "where book_title=? " +
                        "and username=?"+
                "and id =?",
                new String[] {title,username,String.valueOf(id)});
        try {
            cursorUser.moveToFirst();
            byte[] bitmapUser = cursorUser.getBlob(3);
            Bitmap imageUser = BitmapFactory.decodeByteArray(bitmapUser, 0, bitmapUser.length);
            return imageUser;
        }
        finally {
            cursorUser.close();
        }
    }


    public String getName(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username=?", new String[] {username});
        while(cursor.moveToNext()){
            return cursor.getString(2);
        }
        return "";
    }

    public Boolean updatedata(String username, String password, String name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        contentValues.put("name", name);
        long result = MyDB.update("users",  contentValues, "username = ?", new String[]{username});
        if(result == -1) return false;
        else
            return true;
    }

// 사용자가 읽은 책 목록
    public ArrayList<String> getuserbook(String username) {
        ArrayList<String>  bookname = new ArrayList<String>();
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select distinct book_title from user_book where Username=?", new String[]{username});
        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            bookname.add(name);
        }

        return bookname;

    }

}
