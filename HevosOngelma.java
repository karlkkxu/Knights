package demo.d12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("javadoc")
public class HevosOngelma {
    
    //muotoa x (->+), y (-^-)
    public static final int[][] SIIRROT = {
            {1,     2},
            {2,     1},
            {2,    -1},
            {1,    -2},
            {-1,   -2},
            {-2,   -1},
            {-2,    1},
            {-1,    2}
    };

    
    public static class Hevonen {
        private int[] nykyruutu;
        private Lauta lauta;
        private int liikemaara = 0;
        
        public Hevonen(Lauta lauta) {
            this.lauta = lauta;
            this.nykyruutu = new int[] {0, 0};
            teeSiirto(nykyruutu);
        }

        private boolean checkSiirto(int[] siirto) {
            int[] seuraavaRuutu = new int[] {this.nykyruutu[0] + siirto[0], this.nykyruutu[1] + siirto[1]};
            if (this.lauta.onkoRuutu(seuraavaRuutu)) {
                return true;
            }
//            System.out.println(
//                    
//                    "Siirto " + Arrays.toString(siirto) + 
//                    " ei ole laillinen paikasta " + 
//                    Arrays.toString(this.nykyruutu)
//                    
//                    );
            return false;
        }

        private void teeSiirto(int[] ruutu) {
            this.liikemaara++;
            this.nykyruutu = ruutu;
            this.lauta.liikuttu(ruutu, liikemaara);
        }

        public void peruSiirto(int[] ruutu) {
            this.liikemaara--;
            this.lauta.peruSiirto(ruutu);
            
        }
        
    }
    
    
    public static class Lauta {
        private Ruutu[][] ruudukko;
        
        public boolean valmis = false;
        
        public Lauta(int x, int y) {
            this.ruudukko = new Ruutu[x][y];
            for (int i = 0; i < this.ruudukko.length; i++)
                for (int j = 0; j < this.ruudukko[i].length; j++) {
                    this.ruudukko[i][j] = new Ruutu();
                }
        }
        
        public boolean onkoRuutu(int[] seuraavaRuutu) {
            
            if (seuraavaRuutu[0] < 0 || seuraavaRuutu[0] >= this.ruudukko.length)
                return false;
            if (seuraavaRuutu[1] < 0 || seuraavaRuutu[1] >= this.ruudukko[0].length)
                return false;
            
            if (this.ruudukko[seuraavaRuutu[0]][seuraavaRuutu[1]].kayty == true)
                return false;
            
            return true;
        }

        public void liikuttu(int[] ruutu, int liikemaara) {
            this.ruudukko[ruutu[0]][ruutu[1]].merkkaa(liikemaara);
        }

        public void tulosta() {
            for (Ruutu[] rivi : this.ruudukko) {
                System.out.println(Arrays.toString(rivi));
            }
            System.out.println();
        }

        public void peruSiirto(int[] ruutu) {
            this.ruudukko[ruutu[0]][ruutu[1]].merkkaaPois();
            this.tulosta();
            System.out.println("Peruttu!!!");
            
        }

        public void checkValmis() {
            for (int i = 0; i < this.ruudukko.length; i++)
                for (int j = 0; j < this.ruudukko[i].length; j++) {
                    if (this.ruudukko[i][j].kayty == false)
                        return;
                }
            this.valmis = true;
        }
    }
    
    public static class Ruutu {
        private boolean kayty;
        private int mones = -1;
        
        public Ruutu() {
            this.kayty = false;
        }
        
        public void merkkaaPois() {
            this.mones = -1;
            this.kayty = false;
            
        }

        public void merkkaa(int liikemaara) {
            this.kayty = true;
            this.mones = liikemaara;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append('[');
            if (this.kayty)
                if (this.mones < 10)
                    s.append(" " +mones);
                else
                    s.append(mones);
            else
                s.append("  ");
            s.append(']');
            
            return s.toString();
        }
        
    }

    public static void main(String[] args) {
        
        final int LEVEYS = 8;
        final int KORKEUS = 8;
        
        Lauta lauta = new Lauta(KORKEUS, LEVEYS);
        Hevonen heppa = new Hevonen(lauta);
        solve(lauta, heppa);
        lauta.tulosta();
        System.out.println(lauta.valmis);

//        Lauta testi = new Lauta(KORKEUS, LEVEYS);
//        Hevonen testiHeppa = new Hevonen(testi);
//        testiHeppa.liiku(SIIRROT[0]);
//        testiHeppa.liiku(SIIRROT[0]);
//        testiHeppa.liiku(SIIRROT[0]);
//        testiHeppa.liiku(SIIRROT[0]);
//        
//        testi.tulosta();
        

    }

    private static boolean solve(Lauta lauta, Hevonen heppa) {
        
        lauta.checkValmis();
        if (lauta.valmis == true)
            return true;
        
        lauta.tulosta();
        
        List<int[]> mahdollisetSiirrot = new ArrayList<int[]>();
        
        for (int[] siirto : SIIRROT) 
            if (heppa.checkSiirto(siirto) == true)
                mahdollisetSiirrot.add(siirto);
        
        int[] ekaSijainti = new int[] {heppa.nykyruutu[0], heppa.nykyruutu[1]};
        
        for (int[] siirto : mahdollisetSiirrot) {
            heppa.nykyruutu = ekaSijainti;
            int[] seuraavaRuutu = new int[] {heppa.nykyruutu[0] + siirto[0], heppa.nykyruutu[1] + siirto[1]};
            heppa.teeSiirto(seuraavaRuutu);
            if (solve(lauta, heppa) == true)
                return true;
        }
        
        heppa.peruSiirto(ekaSijainti);
        return false;
    }

}
