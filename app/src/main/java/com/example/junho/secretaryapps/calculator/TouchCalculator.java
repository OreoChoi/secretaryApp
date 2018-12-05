package com.example.junho.secretaryapps.calculator;

import android.util.Log;

import java.util.ArrayList;

public class TouchCalculator {
    private static final int SUM = 0, SUBTRACT = 1, DIVISION = 2, MULTIPLY = 3;
    ArrayList<Double> factor = new ArrayList<>();
    ArrayList<String> mathematics = new ArrayList<>();

    /* 계산 후 결과를 리턴합니다. */
    public String calculate() throws NumberFormatException {
        Double result = factor.get(0);

            for (int i = 0; i < mathematics.size(); i++) {
                if (mathematics.get(i) == "+") {
                    result = result + factor.get(i + 1);
                } else if (mathematics.get(i) == "-") {
                    result = result - factor.get(i + 1);
                } else if (mathematics.get(i) == "×") {
                    result = result * factor.get(i + 1);
                } else if (mathematics.get(i) == "÷") {
                    result = result / factor.get(i + 1);
                } else if (mathematics.get(i) == "%") {
                    result = result % factor.get(i + 1);
                } else {
                    return "알수 없는 기호가 도중에 섞였습니다.";
                }
            }
            clear();
        return Double.toString(result);
    }

    /* 인자를 ArrayList에 setting합니다. */
    public void setFactor(String s) {
        factor.add(Double.parseDouble(s));
    }

    /* 수학기호를 ArrayList에 setting 합니다. */
    public void setMathematics(String s) {
        mathematics.add(s);
    }

    /* calResult 메서드 실행 중 lastWord가 수학기호라면 삭제하기 위한 메서드입니다. */
    public void deleteLastMathematics(){ mathematics.remove(mathematics.size()-1);}

    /* 수학기호 List의 크기를 반환합니다. */
    public int getMathematicsSize() {
        return mathematics.size();
    }

    /* 문자열에서 인자를 분리해 저장합니다. */
    public String cutFactor(String s) {
        String temp = s;
        int mathSize = getMathematicsSize();
        int index;

        for (int i = 0; i < mathSize; i++) {
            index = temp.indexOf(mathematics.get(i));
            temp = temp.substring(index + 1, temp.length());
        }

        return temp;
    }

    /* 인자와, 수학기호 리스트를 전부 clear합니다.*/
    public void clear() {
        factor.clear();
        mathematics.clear();
    }
}
