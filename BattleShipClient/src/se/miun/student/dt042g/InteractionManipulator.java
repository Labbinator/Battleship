package se.miun.student.dt042g;


public interface InteractionManipulator {
    
    void rotateShip();
    
    void clickOnTile(int x, int y, BoardPanel panel);
    
    void unMark(int x, int y, BoardPanel panel);
    
    void mark(int x, int y, BoardPanel panel);
}

