import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
public class Puzzle {
    private Tile[][] tile;
    private int BlankPosition;
    public Puzzle(){
        this.tile = new Tile[4][4];
        ArrayList<Integer> x = new ArrayList<>();
        for (int i = 0; i < 16; i++){
            x.add(i+1);
        }
        Collections.shuffle(x);
        int k = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (x.get(k) != 16){
                    tile[i][j] = new OccupiedTile(x.get(k));
                } else {
                    tile[i][j] = new EmptyTile();
                }
                k++;
            }
        }
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (tile[i][j].getNum() == 16){
                    BlankPosition = getTilePosition(i, j);
                    break;
                }
            }
        }
    }
    public Puzzle(String filename) {
        readFileToPuzzle(filename);
        int i;
        int j;
        for (i = 0; i < 4; i++){
            for (j = 0; j < 4; j++){
                if (tile[i][j].getNum() == 16){
                    BlankPosition = getTilePosition(i, j);
                    break;
                }
            }
        }
    }

    public Puzzle(Puzzle copy){
        this.tile = new Tile[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                this.tile[i][j] = copy.tile[i][j];
            }
        }
        this.BlankPosition = copy.BlankPosition;
    }
    public Tile getTile(int i , int j){
        return this.tile[i][j];
    }
    public void readFileToPuzzle(String filename){
        try {
            File file = new File("../test/" + filename);
            Scanner input = new Scanner(file);
            tile = new Tile[4][4];
            while (input.hasNextLine()){
                
                for (int i = 0; i < 4; i++){                    
                    for (int j = 0; j < 4; j++){
                        if (input.hasNextInt()){
                            if (input.nextInt() != 16){
                                tile[i][j] = new OccupiedTile(input.nextInt()); 
                            } else {
                                tile[i][j] = new EmptyTile();
                            }
                        }
                    }
                }
            }

            input.close();
        } catch (FileNotFoundException e){
            System.out.println("File Not Found!");
        }
    }
    public int getTilePosition(int i, int j){
        return ((4*i) + j + 1);
    }
    public int getRow(int position){
        if (position < 5){
            return 0;
        } else if (position < 9){
            return 1;
        } else if (position < 13){
            return 2;
        } else {
            return 3;
        }
    }
    public int getCol(int position){
        if (position % 4 == 0){
            return 3;
        } else {
            return (position % 4) - 1;
        }

    }
    public int getBlankPosition(){
        return this.BlankPosition;
    }
    public void setTile(Object t, int position){
        if (t instanceof Tile){
            this.tile[getRow(position)][getCol(position)] = (Tile) t;
        }
    }

    public boolean isValidMove(String dir){
        if (dir.equals("up")){
            return (getBlankPosition() > 4);
        } else if (dir.equals("down")){
            return (getBlankPosition() < 13);
        } else if (dir.equals("right")){
            return (getBlankPosition() % 4 != 0);
        } else {
            return (getBlankPosition() % 4 != 1);
        }
    }

    public Puzzle up(){
        // Temp menyimpan nilai tile diatasnya
        if (isValidMove("up")){
            Puzzle child = (Puzzle)this;
            int temp = getTile(getRow(getBlankPosition()) - 1, getCol(getBlankPosition())).getNum();
            OccupiedTile ot = new OccupiedTile(temp);
            EmptyTile et = new EmptyTile();
            child.setTile(ot, getBlankPosition());
            child.setTile(et, getBlankPosition() - 4);
            child.BlankPosition = getBlankPosition() - 4;
            return child;
        } else {
            return this;
        }

    }

    public Puzzle down(){
        if (isValidMove("down")){
            Puzzle child = (Puzzle)this;
            int temp = getTile(getRow(getBlankPosition()) + 1, getCol(getBlankPosition())).getNum();
            OccupiedTile ot = new OccupiedTile(temp);
            EmptyTile et = new EmptyTile();
            child.setTile(ot, getBlankPosition());
            child.setTile(et, getBlankPosition() + 4);
            child.BlankPosition = getBlankPosition() + 4;
            return child;
        } else {
            return this;
        }

    }
    public Puzzle right(){
        if (isValidMove("right")){
            Puzzle child = (Puzzle)this;
            int temp = getTile(getRow(getBlankPosition()), getCol(getBlankPosition() + 1)).getNum();
            OccupiedTile ot = new OccupiedTile(temp);
            EmptyTile et = new EmptyTile();
            child.setTile(ot, getBlankPosition());
            child.setTile(et, getBlankPosition() + 1);
            child.BlankPosition = getBlankPosition() + 1;
            return child;
        } else {
            return this;
        }

    }
    public Puzzle left(){
        if (isValidMove("left")){
            Puzzle child = (Puzzle)this;
            int temp = getTile(getRow(getBlankPosition()), getCol(getBlankPosition() - 1)).getNum();
            OccupiedTile ot = new OccupiedTile(temp);
            EmptyTile et = new EmptyTile();
            child.setTile(ot, getBlankPosition());
            child.setTile(et, getBlankPosition() - 1);
            child.BlankPosition = getBlankPosition() - 1;
            return child;
        } else {
            return this;
        }

    }
    public Puzzle moveByString(String dir){
        if (dir.equals("up")){
            return up();
        } else if (dir.equals("down")){
            return down();
        } else if (dir.equals("right")){
            return right();
        } else {
            return left();
        }
    }
    public boolean isFinalState(){
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (this.tile[i][j].getNum() != getTilePosition(i, j)){
                    return false;
                }
            }
        }
        return true;
    }
    public void printTiles(){
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (this.tile[i][j].getNum() < 10){
                    System.out.print(this.tile[i][j].getNum() + "  ");
                } else {
                    if (this.tile[i][j].getNum() != 16){
                        System.out.print(this.tile[i][j].getNum() + " ");
                    } else {
                        System.out.print("X  ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}
