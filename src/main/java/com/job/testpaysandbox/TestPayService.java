package com.job.testpaysandbox;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class TestPayService implements TestPay {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] nums = new int[5];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = in.nextInt();
        }
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
        }
        System.out.println();
    }
}
