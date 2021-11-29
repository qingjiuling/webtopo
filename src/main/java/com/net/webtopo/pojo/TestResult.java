package com.net.webtopo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestResult {

    private String input;
    private String output;

    public TestResult(String input, String output) {
        this.input = input;
        this.output = output;
    }

}
