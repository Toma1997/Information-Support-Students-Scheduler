
/*
Univerzitet poseduje moćan računar, koji koristi za potrebe naučnih istraživanja.
Računar u svako doba mora da ima dobru tehničku podršku i pruža manje
programerske usluge. Informatičku podršku radnim danom pruža 6 studenata
osnovnih i master studija, čiji se raspored rada prilagođava nastavnim obavezama.
Studenti imaju nejednako znanje i iskustvo, tako da imaju različitu cenu radnog sata.

Minimalni broj radnih sati studenata osnovnih studija (1-4) je 8, a master studenata
(5-6) je 7 sati nedeljno. Studenati rade na informatičkoj podršci samo radnim danom
08,00-22,00, tako što je istovremeno angažovan samo po jedan student.
Izraditi plan angažovanja studenata na informatičkoj podršci, koji obezbeđuje
minimalne troškove univerziteta.

*/

package spo.prvi.kolokvijum;

public class InfoPodrskaZadatak {

    static int ukupanTrosak = 0;

    public static void main(String[] args) {

        int [][] studentiBrSatiPoDanima = { //min Sati Dnevno, cena, 5*sati na dan
                                            {8, 1900, 5, 5, 5, 0, 5}, // prvi student
                                            {8, 2000, 4, 8, 4, 0, 4}, // drugi student
                                            {8, 2100, 6, 0, 6, 0, 6}, // treci student:
                                            {8, 2200, 0, 6, 0, 6, 0}, // cetvrti student
                                            {7, 2400, 3, 0, 3, 8, 0}, // peti student
                                            {7, 2500, 0, 0, 0, 6, 2}, // sesti student
                                            };

        int [][] plan = napraviPlanAngazovanja(studentiBrSatiPoDanima);

        for(int i = 0; i < plan[0].length; i++){
            System.out.print((i+1) + ". radni dan angozavani su: ");
            for(int j = 0; j < plan.length; j++){
                if(plan[j][i] != 0){
                    System.out.print(" Student " + (j+1) + ": " + plan[j][i] +  " sati,");
                }
            }
            System.out.println();
        }

        System.out.println("Ukupan trosak univerziteta u dinarima na nedejlnom nivou: " + ukupanTrosak);
    }

    // Asimptotska slozenost vremenski i prostorno je O(n*m)
    public static int [][] napraviPlanAngazovanja(int [][] studentiSatiPoDanima) {

        int[][] planAngazovanja = new int[studentiSatiPoDanima.length][studentiSatiPoDanima[0].length - 2];

        int [] maxBrojSvihSatiPoStudentu = {0, 0, 0, 0, 0, 0}; // Max broj koliko student moze uopste po danu
        int maxBrSatiDnevno = 14;

        int[] nedeljnoUkupnoSatiPoStudentu = {0, 0, 0, 0, 0, 0};
        int [] brSvihStudentskihSatiPoDanu = {0, 0, 0, 0, 0};

        // Inicijalizacija plana.
        planAngazovanja = initializePlan(planAngazovanja);

        // Dodaj za svaki dan sve sate koje studenti mogu da dezuraju
        for(int i = 2; i < studentiSatiPoDanima[0].length; i++) { // Vrti po danima.
            for(int j = 0; j < studentiSatiPoDanima.length; j++) { // Vrti po studentima.
                planAngazovanja[j][i-2] = studentiSatiPoDanima[j][i];
                maxBrojSvihSatiPoStudentu[j] += studentiSatiPoDanima[j][i];
                nedeljnoUkupnoSatiPoStudentu[j] += studentiSatiPoDanima[j][i];
                brSvihStudentskihSatiPoDanu[i-2] += studentiSatiPoDanima[j][i];
                ukupanTrosak += studentiSatiPoDanima[j][1] * studentiSatiPoDanima[j][i];
            }
        }

        // Kreni od najskupljeg studenta i oduzimaj sate dok se ne dodje do 14 sati i da se tom studentu ne predje ispod nedeljnog uslova
        // Kada se dodje do dana kome ne mogu da se skinu sati a da se nekom ne osteti uslov, za tog najskupljeg studenta koji ima jos sati u drugim danima
        // kada se dodje do tog dana za trenutnog studenta, da se ne uzmu svi sati ako ima jos neki dan sate, vec maksimalno koliko moze a minimalno da se oduzme od jeftinijeg studenta
        // nastaviti dalje sa ostatkom sati i opet dodati maksimalno koliko moze skupom a minimalno jeftinijem (osim ako je poslednji dan kad mora da se doda sve ostalo iz uslova)

        int [][] pozajmiceIDaniPoStudentu = {{-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0},}; // Koliko je student pozajmio koji dan, par {dan, pozajmica} po studentu.

        for(int i = 0; i < planAngazovanja[0].length; i++) { // Vrti po danima.
            int indeksStudenta = -1; // Indeks studenta koji ce potencijalno imati pozajmicu trenutnog dana.
            for(int j = planAngazovanja.length - 1; j >= 0; j--) { // Vrti po studentima.

                // Ako je preko 14 sati, student dezura taj dan, i ti sati koje dezura ako se oduzmu od svih njegovih sati ne remete uslov.
                if(brSvihStudentskihSatiPoDanu[i] > maxBrSatiDnevno && planAngazovanja[j][i] > 0){
                    int ostaloSatiZaDan = brSvihStudentskihSatiPoDanu[i] - maxBrSatiDnevno;

                    // Ako je ostalo vise sati nego sto taj student ima tog dana, i ako ti sati ne remete uslov, oduzmi sve.
                    if(ostaloSatiZaDan >= planAngazovanja[j][i] && nedeljnoUkupnoSatiPoStudentu[j] - planAngazovanja[j][i] >= studentiSatiPoDanima[j][0]){
                        nedeljnoUkupnoSatiPoStudentu[j] -= planAngazovanja[j][i];
                        brSvihStudentskihSatiPoDanu[i] -= planAngazovanja[j][i];
                        ukupanTrosak -= studentiSatiPoDanima[j][1] * planAngazovanja[j][i];
                        planAngazovanja[j][i] -= planAngazovanja[j][i];

                    } else { // Ako je broj sati manji od prekoracenja onda samo taj deo prekoracenja uzmi i da ne ide ispod uslova.
                        if(nedeljnoUkupnoSatiPoStudentu[j] - ostaloSatiZaDan >= studentiSatiPoDanima[j][0]){
                            planAngazovanja[j][i] -= ostaloSatiZaDan;
                            brSvihStudentskihSatiPoDanu[i] -= ostaloSatiZaDan;
                            nedeljnoUkupnoSatiPoStudentu[j] -= ostaloSatiZaDan;
                            ukupanTrosak -= studentiSatiPoDanima[j][1] * ostaloSatiZaDan;
                        } else { // Ako taj ostatak dana spusta ispod uslova ako se oduzme od trenutnih sati studenta na nedeljnom nivou.
                            int razlikaUslovaISvihSatiStudenta = nedeljnoUkupnoSatiPoStudentu[j] - studentiSatiPoDanima[j][0];

                            planAngazovanja[j][i] -= razlikaUslovaISvihSatiStudenta;
                            brSvihStudentskihSatiPoDanu[i] -= razlikaUslovaISvihSatiStudenta;
                            nedeljnoUkupnoSatiPoStudentu[j] -= razlikaUslovaISvihSatiStudenta;
                            ukupanTrosak -= studentiSatiPoDanima[j][1] * razlikaUslovaISvihSatiStudenta;

                            // Potencijalna pozajmica koja ce se za tog studenta na druge dane raspodeliti
                            int privremenaPotencijalnaPozajmica = brSvihStudentskihSatiPoDanu[i] - maxBrSatiDnevno;

                            // Ako taj student ima neiskoriscene sate negde vece od pozajmice, i ako je i dalje prekoraceno sati trenutnog dana.
                            if(maxBrojSvihSatiPoStudentu[j] - nedeljnoUkupnoSatiPoStudentu[j] > privremenaPotencijalnaPozajmica &&
                               brSvihStudentskihSatiPoDanu[i] > maxBrSatiDnevno){
                                pozajmiceIDaniPoStudentu[j][0] = i;
                                pozajmiceIDaniPoStudentu[j][1] = privremenaPotencijalnaPozajmica;
                                indeksStudenta = j;
                            }
                        }
                    }
                }
            }
            // Ako je neki student sacuvan za pozajmicu.
            if(indeksStudenta >= 0){
                // Ako se ipak spustio broj dana do ili ispod max, resetuj na 0 pozajmicu i dan na -1.
                if(brSvihStudentskihSatiPoDanu[i] <= maxBrSatiDnevno){
                    pozajmiceIDaniPoStudentu[indeksStudenta][0] = -1;
                    pozajmiceIDaniPoStudentu[indeksStudenta][1] = 0;
                } else {
                    planAngazovanja[indeksStudenta][i] -= pozajmiceIDaniPoStudentu[indeksStudenta][1];
                    brSvihStudentskihSatiPoDanu[i] -= pozajmiceIDaniPoStudentu[indeksStudenta][1];
                }
            }
        }

        // Za studente sa pozajmicom rasporedi po danima gde nisu dezurali i nek uzmu sto manje sati od jeftinijih studenata.
        for(int i = 0; i < planAngazovanja.length; i++){ // Vrti po studentima.
            if(pozajmiceIDaniPoStudentu[i][0] > -1 && pozajmiceIDaniPoStudentu[i][1] > 0){ // Ako neko ima pozajmicu.
                for(int j = 0; j < planAngazovanja[0].length; j++){ // Vrti po danima.
                    if(pozajmiceIDaniPoStudentu[i][0] != j && pozajmiceIDaniPoStudentu[i][1] > 0){ // Pogledaj sve dane osim kada je uzeo pozajmicu.
                        // Nadji jeftinijeg koji ima vise sati od neiskoriscenih tog dana za studenta sa pozajmicom sati.
                        int indeksJeftiniji = i-1;
                        while(planAngazovanja[indeksJeftiniji][j] <= studentiSatiPoDanima[i][j+2]){
                            indeksJeftiniji--;
                        }
                        // Razlika sati jeftinijeg studenta i nesikoriscenih sati studenta sa pozajmicom.
                        int razlikaSatiJeftinijegINeiskoriscenihSatiSkupljeg = planAngazovanja[indeksJeftiniji][j] - studentiSatiPoDanima[i][j+2];

                        int satiZaDodavanjePozajmljenom = 0;
                        // Ako je razlika ta manja od neiskoriscenih sati studenta i ako se za tu razliku oduzme od jeftinijeg da ne remeti uslov.
                        if(razlikaSatiJeftinijegINeiskoriscenihSatiSkupljeg <= studentiSatiPoDanima[i][j+2] &&
                          nedeljnoUkupnoSatiPoStudentu[indeksJeftiniji] - razlikaSatiJeftinijegINeiskoriscenihSatiSkupljeg >= studentiSatiPoDanima[indeksJeftiniji][0]){
                            satiZaDodavanjePozajmljenom = razlikaSatiJeftinijegINeiskoriscenihSatiSkupljeg;

                        } else { // Ako je razlika veca od svih neiskoriscenih sati tog dana za studenta sa pozajmicom, dodaj mu sve sate tog dana

                            // Ako se dodaju svi sati tog dana kojih je manje ili jednako od pozajmice i ako je kad se dodaju svi ti neiskorisceni sati jednaki minimalnom uslovu
                            if(studentiSatiPoDanima[i][j+2] == pozajmiceIDaniPoStudentu[i][1] &&
                                nedeljnoUkupnoSatiPoStudentu[i] + studentiSatiPoDanima[i][j+2] == studentiSatiPoDanima[i][0]){
                                satiZaDodavanjePozajmljenom = studentiSatiPoDanima[i][0] - nedeljnoUkupnoSatiPoStudentu[i];
                            } else { // Ako kad se dodaju svi neiskorisceni sati i dalje ne doseze minimalan nedeljni uslov sati za studenta
                                satiZaDodavanjePozajmljenom = studentiSatiPoDanima[i][j+2];
                            }
                        }
                        // Oduzmi sate jeftinijem i azuriraj ostale vrednosti za sate i trosak.
                        planAngazovanja[indeksJeftiniji][j] -= satiZaDodavanjePozajmljenom;
                        nedeljnoUkupnoSatiPoStudentu[indeksJeftiniji] -= satiZaDodavanjePozajmljenom;
                        ukupanTrosak -= studentiSatiPoDanima[indeksJeftiniji][1] * satiZaDodavanjePozajmljenom;

                        // Dodaj deo neiskoriscenih sati studentu sa pozajmicom i azuriraj pozajmicu.
                        planAngazovanja[i][j] += satiZaDodavanjePozajmljenom;
                        pozajmiceIDaniPoStudentu[i][1] -= satiZaDodavanjePozajmljenom;
                    }
                }
            }
        }

        return planAngazovanja;

    }

    // Inicijalizacija plana angazovanja
    public static int[][] initializePlan(int [][] emptyPlan){
        for(int i = 0; i < emptyPlan.length; i++){
            for(int j = 0; j < emptyPlan[0].length - 1; j++){
                emptyPlan[i][j] = 0;
            }
        }
        return emptyPlan;
    }
}
