package main;

import assembler.Assembler;
import spring.chap03.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainForAssembler {
    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("명령어 입력");
            String command = reader.readLine();
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("종료함.");
                break;
            }
            if (command.startsWith("new")) {
                processNewCommand(command.split(" "));
                continue;
            } else if (command.startsWith("change")) {
                processChangeCommand(command.split(" "));
                continue;
            }
            printHelp();
        }
    }

    private static Assembler assembler = new Assembler();

    private static void processNewCommand(String[] arg) {
        if (arg.length != 5) {
            printHelp();
            return;
        }

        MemberRegisterService regSvc = assembler.getMemberRegisterService();
        RegisterRequest req = new RegisterRequest();
        req.setEmail(arg[1]);
        req.setName(arg[2]);
        req.setPassword(arg[3]);
        req.setConfirmPassword(arg[4]);

        if (!req.isPasswordEqualToConfirmPassword()) {
            System.out.println("암호와 확인이 일치하지 않음.\n");
            return;
        }
        try {
            regSvc.regist(req);
            System.out.println("등록완료.\n");
        } catch (DuplicateMemberException e) {
            System.out.println("이미 존재하는 이메일.\n ");
        }
    }

    private static void processChangeCommand(String[] arg) {
        if (arg.length != 4) {
            printHelp();
            return;
        }
        ChangePasswordService changePasswordSvc =
                assembler.getChangePasswordService();
        try {
            changePasswordSvc.changePassword(arg[1], arg[2], arg[3]);
            System.out.println("암호 변경\n");
        } catch (MemberNotFoundException e) {
            System.out.println("존재하지 않는 이메일.\n");
        } catch (WrongIdPasswordException e) {
            System.out.println("이메일과 암호가 일치하지 않음.\n");
        }
    }
    private static void printHelp(){
        System.out.println();
        System.out.println("잘못된 명령어. 명령어 사용법을 확인.");
        System.out.println("명령어 사용법");
        System.out.println("new 이메일 이름 암호 암호확인");
        System.out.println("chahge 이메일 현재비번 변경비번");
        System.out.println();
    }

}
