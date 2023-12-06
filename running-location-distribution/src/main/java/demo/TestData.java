package demo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TestData {
    private String word1;
    private String word2;
    // private int heartRate;


}
