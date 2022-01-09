package com.net.webtopo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestResult {
    private int caseId;
    private String input;
    private String output;

    public TestResult(int id, String input, String output) {
        this.caseId = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public String toString() {
        return "\n-------------TestResult------------{\n" +
                "id=" + caseId + "\n" +
                "input=" + input + "\n" +
                "output=\n" + output +
                "}\n";
    }
}
