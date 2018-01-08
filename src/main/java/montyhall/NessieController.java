package montyhall;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class NessieController {

    private List<NessieGame> games = new ArrayList<>();
    private final List<String> lakes = new ArrayList<>(Arrays.asList("L1", "L2", "L3"));

    @RequestMapping("/find")
    public String find(@RequestParam(value="id") int id, @RequestParam(value="lake") String lake) {

        //check if game already exists
        boolean found = games.stream().anyMatch(game -> game.getId() == id);

        if(found) {
            //game already exists, return the lake where Nessie lives
            String correctLake = games.stream()
                    .filter(game -> game.getId() == id)
                    .findFirst().get().getLake();

            games.removeIf(game -> game.getId() == id);

            return correctLake;


        } else {
            //create a new game and set which lake Nessie lives in
            Collections.shuffle(lakes);
            String nessieLake = lakes.get(0);

            NessieGame newGame = new NessieGame(id, nessieLake);
            games.add(newGame);

            //return a lake where Nessie does not live
            return lakes.stream()
                    .filter(s-> !s.equals(nessieLake) && !s.equals(lake))
                    .findFirst().get();
        }
    }
}
