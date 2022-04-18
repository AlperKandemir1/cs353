package com.company;
import java.sql.*;
import java.lang.Math;
public class Main {
    public static void main(String[] args)throws  SQLException {
        Connection con=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String username = "alper.kandemir";
            String password = "h41ELpmI";
            String dbname = "alper_kandemir";
            con =  DriverManager.getConnection("jdbc:mysql://dijkstra.ug.bcc.bilkent.edu.tr/" + dbname,username,password);
            System.out.println("connected");
            String str;
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        Statement stmt = null;
        stmt = con.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS apply");
        stmt = con.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS student");
        stmt = con.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS company");

        //table names
        String studentTable;
        String companyTable;
        String applyTable;

        //student table
        studentTable= "Create table student("
                + "sid CHAR(12), "
                + "sname VARCHAR(50), "
                + "bdate DATE, "
                + "address VARCHAR(50), "
                + "scity VARCHAR(20), "
                + "year CHAR(20), "
                + "gpa FLOAT, "
                + "nationality VARCHAR(20), "
                + "PRIMARY KEY (sid)) ENGINE=INNODB";
        stmt = con.createStatement();
        stmt.executeUpdate(studentTable);
        System.out.println("Student table created");
        //company table
        companyTable="Create table company("
                + "cid CHAR(8), "
                + "cname VARCHAR(20), "
                + "quota int, "
                + "gpathreshold float, "
                + "PRIMARY KEY (cid)) ENGINE=INNODB";
        stmt = con.createStatement();
        stmt.executeUpdate(companyTable);
        System.out.println("Company table created");
        //apply table
        applyTable="Create table apply("
                + "sid CHAR(12), "
                + "cid CHAR(8), "
                + "PRIMARY KEY(sid,cid),"
                + "FOREIGN KEY (sid) REFERENCES student(sid) ON UPDATE CASCADE ON DELETE RESTRICT, "
                + "FOREIGN KEY (cid) REFERENCES company(cid)ON UPDATE CASCADE ON DELETE RESTRICT) ENGINE=INNODB";
        stmt = con.createStatement();
        stmt.executeUpdate(applyTable);
        System.out.println("Apply table created");

        //insert student
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO student VALUES('21000001','Marco','1998-05-31','Strobelallee','Dortmund','senior','2.64','DE')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO student VALUES('21000002','Arif','2001-05-17','Nisantasi','Istanbul','junior','3.86','TC')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO student VALUES('21000003','Veli','2003-02-19','Cayyolu','Ankara','freshman','2.21','TC')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO student VALUES('21000004','Ayse','2003-05-01','Tunali','Ankara','freshman','2.52','TC')");
        //insert company
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO company VALUES('C101','milsoft','3','2.50')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO company VALUES('C102','merkez bankasi','10','2.45')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO company VALUES('C103','tubitak','2','3.00')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO company VALUES('C104','havelsan','5','2.00')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO company VALUES('C105','aselsan','4','2.50')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO company VALUES('C106','tai','2','2.20')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO company VALUES('C107','amazon','1','3.85')");

        //insert apply
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO apply VALUES('21000001','C101')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO apply VALUES('21000001','C102')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO apply VALUES('21000001','C104')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO apply VALUES('21000002','C107')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO apply VALUES('21000003','C104')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO apply VALUES('21000003','C106')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO apply VALUES('21000004','C102')");
        stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO apply VALUES('21000004','C106')");

        System.out.println(" ");
        String query1 = "SELECT sname FROM student s WHERE s.sid IN(SELECT sid FROM apply GROUP BY sid HAVING COUNT(*)=3)";
        String query2 = "SELECT sum(c.quota) FROM company c, student s, apply a WHERE s.sid = a.sid AND c.cid = a.cid AND s.sid = (" +
                        "SELECT T.s_id FROM (SELECT sid as s_id, count(cid) as cnt FROM apply GROUP BY sid) as T HAVING max(T.cnt))";
        String query3 = "SELECT  T.cnt/T.scnt as average,T.avg FROM (SELECT count(a.cid) as cnt, count( distinct a.sid) as scnt, s.nationality as avg FROM  student s,apply a WHERE a.sid = s.sid GROUP BY nationality) T";
        String query4 = "SELECT DISTINCT cname " +
                "FROM apply a,company c,student s " +
                "WHERE c.cid = a.cid and s.sid=a.sid and s.year IN(SELECT s1.year FROM student s1 where s1.year = \"freshman\")";

        String query5 = "SELECT c.cid, AVG(gpa) FROM student s, apply a , company c WHERE s.sid = a.sid AND c.cid = a.cid GROUP BY c.cid ";

        stmt = con.createStatement();
        ResultSet rs1 = stmt.executeQuery(query1);
        while (rs1.next()) {
            System.out.println("Name: " + rs1.getString("sname")+"\n");
        }

        System.out.println("---------------------");
        System.out.println(" ");
        stmt = con.createStatement();
        ResultSet rs2 = stmt.executeQuery(query2);
        while (rs2.next()) {
           System.out.print("Sum of quotas: " +rs2.getString(1)+"\n");
        }
        System.out.println(" ");
        System.out.println("---------------------");


        stmt = con.createStatement();
        ResultSet rs3 = stmt.executeQuery(query3);
        while (rs3.next()) {
            System.out.print("avg: " + rs3.getString(1)+" Nat: " + rs3.getString(2)+"\n");
        }
        System.out.println(" ");
        System.out.println("---------------------");
        System.out.println(" ");

       stmt = con.createStatement();
        ResultSet rs4 = stmt.executeQuery(query4);
        while (rs4.next()) {
            System.out.print("Cname: " + rs4.getString(1)+ "\n");
        }
        System.out.println(" ");
        System.out.println("---------------------");
        System.out.println(" ");

        stmt = con.createStatement();
        ResultSet rs5 = stmt.executeQuery(query5);
        while (rs5.next()) {
            System.out.print("cid: "+ rs5.getString(1)+ ", avg gpa= "+ rs5.getDouble(2)+"\n" );

        }
        System.out.println(" ");
        System.out.println("---------------------");

    }
}

