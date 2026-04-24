package onlineexam.util;

public class GenerateHash {

    public static void main(String[] args) {

        System.out.println("Admin Password Hash:");
        System.out.println(
            PasswordUtil.hashPassword("admin123")
        );

        System.out.println();

        System.out.println("Examiner Password Hash:");
        System.out.println(
            PasswordUtil.hashPassword("examiner123")
        );

        System.out.println();

        System.out.println("Ashish's Password Hash:");
        System.out.println(
            PasswordUtil.hashPassword("ashish123")
        );

        System.out.println();

        System.out.println("Dhruv's Password Hash:");
        System.out.println(
            PasswordUtil.hashPassword("dhruv123")
        );

        System.out.println();

        System.out.println("Paras's Password Hash:");
        System.out.println(
            PasswordUtil.hashPassword("paras123")
        );
    }
}