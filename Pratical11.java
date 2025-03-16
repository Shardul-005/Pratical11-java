import java.util.*;
import java.io.*;

class Pratical11 {
    private String[][] layout, userAns;
    private int[] IndexRemove;
    private int size, remove;
    private boolean valid_level;

    Game2(int size) throws IOException {
        this.size = size;
        this.layout = new String[size][size];
        this.userAns = new String[size][size];
        this.IndexRemove = new int[size * size];
        setupGame();
        playGame();
    }

    private void setgame() throws IOException {
        getLayout();
        selectLvl();
        elementRemove();
        grid();
    }

    private void playgame() throws IOException {
        askValue();
        copyLayoutToUserAns();

        if (checkSolution()) {
            System.out.println("Correct Answer");
        } else {
            System.out.println("Wrong Answer");
        }
    }

    private void grid() {
        StringBuilder sb = new StringBuilder();
        String[][] arr = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                arr[i][j] = String.format("%-3s", layout[i][j].equals(" ") ? " " : layout[i][j]);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(".---");
            }
            sb.append(".\n");
            for (int j = 0; j < size; j++) {
                sb.append("|").append(arr[i][j]);
            }
            sb.append("|\n");
        }
        for (int i = 0; i < size; i++) {
            sb.append(".---");
        }
        sb.append(".\n");

        System.out.print(sb.toString());
    }

    private Integer[] getArray() {
        Integer[] pn = new Integer[size];
        for (int i = 0; i < size; i++) {
            pn[i] = i + 1;
        }
        return pn;
    }

    private List<Integer> checkColumn(List<Integer> list, int row, int col) {
        for (int a = row; a >= 0; a--) {
            for (int b = 0; b < list.size(); b++) {
                if (Integer.parseInt(layout[a][col]) == list.get(b)) {
                    list.remove(b);
                }
            }
        }
        return list;
    }

    private void getLayout() {
        for (int i = 0; i < size; i++) {
            Integer[] pn = getArray();
            List<Integer> pnList = new ArrayList<>(Arrays.asList(pn));
            for (int j = 0; j < size; j++) {
                List<Integer> newList = new ArrayList<>(pnList);
                newList = checkColumn(newList, i - 1, j);
                if (newList.size() == 0) {
                    i = 0;
                    break;
                }
                int r = (int) (Math.random() * 10 % newList.size());
                layout[i][j] = newList.get(r).toString();
                pnList.remove(newList.remove(r));
            }
        }
    }

    private void selectLvl() throws IOException {
        System.out.println("Select difficulty level:\n1. Easy\n2. Medium\n3. Hard");
        BufferedReader gamelevel = new BufferedReader(new InputStreamReader(System.in));

        while (!valid_level) {
            String level = gamelevel.readLine();
            valid_level = true;
            switch (level) {
                case "1":
                    remove = size * size / 2;
                    break;
                case "2":
                    remove = size * size / 3;
                    break;
                case "3":
                    remove = size * size * 2 / 3;
                    break;
                default:
                    System.out.println("Invalid option");
                    valid_level = false;
                    break;
            }
        }
    }

    private void elementRemove() {
        IndexRemove = new int[2 * remove];
        for (int delete = 0; delete < remove; delete++) {
            int i = (int) (Math.random() * 10 % size);
            int j = (int) (Math.random() * 10 % size);
            if (layout[i][j].equals(" ")) {
                delete--;
                continue;
            }
            layout[i][j] = " ";
            IndexRemove[2 * delete] = i + 1;
            IndexRemove[2 * delete + 1] = j + 1;
        }
    }

    private void askValue() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (int value = 0; value < remove; value++) {
            System.out.println("Z: Undo");
            System.out.println("Enter value for index " + IndexRemove[2 * value] + "," + IndexRemove[2 * value + 1]);

            String input = reader.readLine();

            if (input.equalsIgnoreCase("z") && value > 0) {
                layout[IndexRemove[2 * (value - 1)] - 1][IndexRemove[2 * (value - 1) + 1] - 1] = " ";
                grid();
                value -= 2;
                continue;
            } else if (!input.matches("\\d+")) {
                System.out.println("Invalid input, numbers allowed from 1 to " + size);
                value--;
                continue;
            } else if ((Integer.parseInt(input) < 1) || (Integer.parseInt(input) > size)) {
                System.out.println("Invalid input, numbers allowed from 1 to " + size);
                value--;
                continue;
            }

            layout[IndexRemove[2 * value] - 1][IndexRemove[2 * value + 1] - 1] = input;
            grid();
        }
    }

    private void copyLayoutToUserAns() {
        for (int i = 0; i < size; i++) {
            userAns[i] = Arrays.copyOf(layout[i], size);
        }
    }

    private boolean checkSolution() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int checkcol = 0; checkcol < size; checkcol++) {
                    if (checkcol == j) {
                        continue;
                    }
                    if (userAns[i][checkcol].equals(userAns[i][j])) {
                        return false;
                    }
                }
                for (int checki = 0; checki < size; checki++) {
                    if (checki == i) {
                        continue;
                    }
                    if (userAns[checki][j].equals(userAns[i][j])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        int size = Integer.parseInt(args[0]);
        new Pratical11(size);
    }
}
