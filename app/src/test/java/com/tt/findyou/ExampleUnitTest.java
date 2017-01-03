package com.tt.findyou;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        List<User> list1 = new ArrayList<>();
        list1.add(new User(1,"aaa"));
        list1.add(new User(2,"bbb"));

        List<User> list2 = list1;


        System.out.println(list2.size()+"-----list2.size");

        list1.remove(1);

        System.out.println(list2.size()+"-----list2.size");
    }
    class User{
        int id;
        String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    @Test
    public void countAge(){
        Calendar curr = Calendar.getInstance();
        int currYear = curr.get(Calendar.YEAR);
        int currMonth = curr.get(Calendar.MONTH);
        int currDate = curr.get(Calendar.DATE);
        System.out.println(currYear);
        System.out.println(currMonth);
        System.out.println(currDate);


        try {
            curr.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("1989-02-03"));
            int birthYear = curr.get(Calendar.YEAR);
            int birthMonth = curr.get(Calendar.MONTH);
            int birthDate = curr.get(Calendar.DAY_OF_MONTH);

            System.out.println(birthYear);
            System.out.println(birthMonth);
            System.out.println(birthDate);

            int age = currYear-birthYear;
            if(currMonth < birthMonth && (currMonth == birthMonth && currDate < birthDate))
                age--;
            System.out.println(age+"----age");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}