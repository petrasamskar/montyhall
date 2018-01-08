package montyhall;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class NessieControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private int countCorrectLakes;
    private int countIncorrectLakes;
    private int gameId;
    private final List<String> lakes = new ArrayList<>(Arrays.asList("L1", "L2", "L3"));

    private final int numberOfIterations = 1000;

    @Before
    public void setUp() {
        countCorrectLakes = 0;
        countIncorrectLakes = 0;
        gameId = 0;
    }

    @Test
    public void changingLakeWillBeBeneficial() throws Exception {

        for(int i = 0; i < numberOfIterations; i++) {

            Collections.shuffle(lakes);

            String startValueToSend = lakes.get(0);
            MvcResult result1 = this.mockMvc.perform(get("/find").param("id", String.valueOf(gameId)).param("lake", startValueToSend))
                    .andExpect(status().isOk())
                    .andReturn();

            String firstResponseLake = result1.getResponse().getContentAsString();

            //filter out a lake that has not already been sent or returned
            String nextValueToSend = lakes.stream()
                    .filter(s-> !s.equals(firstResponseLake) && !s.equals(startValueToSend))
                    .findFirst().get();

            MvcResult result2 = this.mockMvc.perform(get("/find").param("id", String.valueOf(gameId)).param("lake", nextValueToSend))
                    .andExpect(status().isOk())
                    .andReturn();

            addToCounter(result2.getResponse().getContentAsString(), nextValueToSend);

            gameId ++;
        }
        Assert.assertTrue(countCorrectLakes > countIncorrectLakes);
    }

    @Test
    public void stickWithFirstLakeWillNotBeBeneficial() throws Exception {
        for(int i = 0; i < numberOfIterations; i++) {

            Collections.shuffle(lakes);

            String startValueToSend = lakes.get(0);
            this.mockMvc.perform(get("/find").param("id", String.valueOf(gameId)).param("lake", startValueToSend))
                    .andExpect(status().isOk())
                    .andReturn();
            MvcResult result2 = this.mockMvc.perform(get("/find").param("id", String.valueOf(gameId)).param("lake", startValueToSend))
                    .andExpect(status().isOk())
                    .andReturn();

            addToCounter(result2.getResponse().getContentAsString(), lakes.get(0));

            gameId++;
        }
        Assert.assertTrue(countIncorrectLakes > countCorrectLakes);
    }

    private void addToCounter(String returnedLake, String sentLake) {
        if (returnedLake.equals(sentLake)) {
            countCorrectLakes++;
        } else {
            countIncorrectLakes++;
        }
    }
}
