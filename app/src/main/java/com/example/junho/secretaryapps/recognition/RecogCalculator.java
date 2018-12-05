package com.example.junho.secretaryapps.recognition;

public class RecogCalculator {
private final int SUM = 1 ,SUBTRACT = 2,DIVISION = 3,MULTIPLY = 4;

    public String calculate(String s,int selectOperator) throws NumberFormatException {
        int index, temp;
        String result = s;

        if(selectOperator == SUM) {
            if (s.contains("+")) {
                index = s.indexOf("+");
            } else {
                index = s.indexOf("더하기");
            }
        }else if(selectOperator == SUBTRACT) {
            if (s.contains("-")) {
                index = s.indexOf("-");
            } else {
                index = s.indexOf("빼기");
            }
        }else if(selectOperator == DIVISION) {
            if (s.contains("/")) {
                index = s.indexOf("/");
            } else {
                index = s.indexOf("나누기");
            }
        }else if(selectOperator == MULTIPLY) {
            if (s.contains("*")) {
                index = s.indexOf("*");
            } else {
                index = s.indexOf("곱하기");
            }
        }else{
            return result;
        }

        String cutString1 = s.substring(0, index);
        String cutString2 = s.substring(index, s.length());

        cutString1 = cutString1.replaceAll("[^0-9]", "");
        cutString2 = cutString2.replaceAll("[^0-9]", "");

        if(selectOperator == SUM) {
            temp = Integer.parseInt(cutString1) + Integer.parseInt(cutString2);
        }else if (selectOperator == SUBTRACT){
            temp = Integer.parseInt(cutString1) - Integer.parseInt(cutString2);
        }else if (selectOperator == DIVISION){
            temp = Integer.parseInt(cutString1) / Integer.parseInt(cutString2);
        }else if (selectOperator == MULTIPLY){
            temp = Integer.parseInt(cutString1) * Integer.parseInt(cutString2);
        }else{
            temp = -1;
        }

        result = result.concat(" = " +Integer.toString(temp));

        return result;

    }

    public String dutchPay(String s) throws NumberFormatException {
        int index, sum;
        String result = s;

        return result;
    }

    public int searchOperator(String s) {
        if (s.contains("더하기") || s.contains("+")) {
            return 0;
        } else if (s.contains("빼기") || s.contains("-")) {
            return 1;
        } else if (s.contains("나누기") || s.contains("/")) {
            return 2;
        } else if (s.contains("곱하기") || s.contains("*")) {
            return 3;
        } else if (s.contains("더치페이") || s.contains("n 분의 1")) {
            return 4;
        } else {
            return 5;
        }
    }

}
