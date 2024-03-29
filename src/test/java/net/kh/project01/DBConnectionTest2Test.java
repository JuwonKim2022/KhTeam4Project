package net.kh.project01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class DBConnectionTest2Test {
    @Autowired
    DataSource ds;

    @Test
    public void insertUserTest() throws Exception{
        User2 user = new User2("asdf2","12345678","abdula","aaa@aaa.com",new Date(),"facebook",new Date());
        deleteAll();
        int rowCnt = insertUser(user);

        assertTrue(rowCnt==1);
    }

    @Test
    public void selectUserTest() throws Exception{
        deleteAll();
        User2 user = new User2("asdf2","12345678","abdula","aaa@aaa.com",new Date(),"facebook",new Date());
        int rowCnt = insertUser(user);
        User2 user2 = selectUser("asdf2");

        assertTrue(user.getId().equals("asdf2"));
    }

    @Test
    public void deleteUserTest() throws Exception{
        deleteAll();
        int rowCnt = deleteUser("asdf");

        assertTrue(rowCnt==0);

        User2 user = new User2("asdf2","12345678","abdula","aaa@aaa.com",new Date(),"facebook",new Date());
        rowCnt=insertUser(user);
        assertTrue(rowCnt==1);

        rowCnt = deleteUser(user.getId());
        assertTrue(rowCnt==1);

        assertTrue(selectUser(user.getId())==null);
    }

    //매개변수로 받은 사용자 정보로 user_info테이블을 update하는 메서드
    public int updateUser(User2 user) throws Exception{
        return 0;
    }

    public int deleteUser(String id) throws Exception{
        Connection conn = ds.getConnection();
        String sql = "delete from user_info where id= ? ";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
//        int rowCnt = pstmt.executeUpdate();
//        return rowCnt;    아래와 같은거 한 줄로 표시
        return pstmt.executeUpdate();
    }
    public  User2 selectUser(String id) throws Exception{
        Connection conn = ds.getConnection();
        String sql = "select * from user_info where id= ? ";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,id);
        ResultSet rs = pstmt.executeQuery(); //select

        if (rs.next()){
            User2 user = new User2();
            user.setId(rs.getString(1));
            user.setPwd(rs.getString(2));
            user.setName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setBirth(new Date(rs.getDate(5).getTime()));
            user.setSns(rs.getString(6));
            user.setReg_date(new Date(rs.getTimestamp(7).getTime()));

            return user;
        }
        return null;
    }
    private void deleteAll() throws Exception{
        Connection conn = ds.getConnection();
        String sql = "delete from user_info ";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    //사용자 정보를 user_info테이블에 저장하는 메서드
    public int insertUser(User2 user) throws Exception{
        Connection conn = ds.getConnection();

//        insert into user_info (id, pwd, name, email, birth, sns, reg_date)
//        values ('asdf22','12345678','smith','aaa@aaa.com', '2021-01-01', 'facebook', now());

        String sql = "insert into user_info values (?, ?, ?, ?, ?, ?, now()) ";

        PreparedStatement pstmt = conn.prepareStatement(sql); //SQL injection 공격 방어, 성능향상
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getPwd());
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getEmail());
        pstmt.setDate(5, new java.sql.Date(user.getBirth().getTime()));
        pstmt.setString(6, user.getSns());

        int rowCnt = pstmt.executeUpdate(); //insert, delete, update에 사용
        return rowCnt;
    }

    @Test
    public void springJdbcConnectionTest() throws Exception{
//        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
//        DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // 데이터베이스의 연결을 얻는다.

        System.out.println("conn = " + conn);
        assertTrue(conn!=null);
    }
}