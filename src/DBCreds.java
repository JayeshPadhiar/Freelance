public class DBCreds {
    public String url, dbName, dbUrl, user, pass;

    public DBCreds(){
        this.url = "jdbc:mysql://localhost:3306/";
        this.dbName = "freelance";
        this.dbUrl = url+dbName+"?autoReconnect=true";
        this.user = "root";
        this.pass = "password";
    }
}
