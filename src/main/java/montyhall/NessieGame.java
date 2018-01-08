package montyhall;

/*
Nessie game, holds information of which lake Nessie lives in.
 */
class NessieGame {

    private final int id;
    private final String lake;

    public NessieGame(int id, String lake) {
        this.id = id;
        this.lake = lake;
    }

    public int getId() {
        return id;
    }

    public String getLake() {
        return lake;
    }
}
