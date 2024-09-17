package com.ecommerce.entity;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String s = "Prism White, Prism Black, Prism Green, Prism Blue, Canary Yellow, Flamingo Pink, Ceramic Black, Ceramic White, Cardinal Red, Smoke Blue";

        List<String> items = Arrays.asList(s.split("\\s*,\\s*"));
        System.out.println("items = " + items);

    }
}
