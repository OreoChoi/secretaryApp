package com.example.junho.secretaryapps.recognition;

import static com.example.junho.secretaryapps.recognition.RecognitionActivity.CALCULATOR;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.DIVISION;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.EMPTY;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.MAP;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.MEMO;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.MEMO_LIST;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.MODE_CHANGE;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.MODE_RETURN;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.MULTIPLY;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.SUBTRACT;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.SUM;

public class Separation {

    public int wordSeparation(String s) {
        if (s.contains("더하기") || s.contains("+")) {
            return SUM;
        } else if (s.contains("빼기") || s.contains("-")) {
            return SUBTRACT;
        } else if (s.contains("나누기") || s.contains("/")) {
            return DIVISION;
        } else if (s.contains("곱하기") || s.contains("*")) {
            return MULTIPLY;
        } else if (s.contains("메모")) {
            if(s.contains("보여 줘") || s.contains("확인") || s.contains("표시")){
                return MEMO_LIST;
            }else{
                return MEMO;
            }
        } else if (s.contains("목적지 알람")||s.contains("알람")||s.contains("가는 법")) {
            return MAP;
        } else if (s.contains("계산기")) {
            return CALCULATOR;
        } else if (s.contains("모드") || s.contains("모두")) {
            if(s.contains("변경") || s.contains("선택")){
                return MODE_CHANGE;
            }else if(s.contains("확인")){
                return MODE_RETURN;
            }else {
                return EMPTY;
            }
        } else{
            return EMPTY;
        }
    }
}
