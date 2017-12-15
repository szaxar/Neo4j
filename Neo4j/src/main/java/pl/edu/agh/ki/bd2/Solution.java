package pl.edu.agh.ki.bd2;

import org.neo4j.cypher.internal.frontend.v2_3.ast.In;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Solution {

    private final GraphDatabase graphDatabase = GraphDatabase.createDatabase();

    public void databaseStatistics() {
        System.out.println(graphDatabase.runCypher("CALL db.labels()"));
        System.out.println(graphDatabase.runCypher("CALL db.relationshipTypes()"));

    }

    public void runAllTests() {
      //generator();
      // System.out.println(findBookByTitle("Harry Potter"));
      // System.out.println(findReaderByName("Wiktor","Łęczyński"));
     // System.out.print(findHireByID(1));
    //   System.out.print(findRelationBy("Wydawnictwo","nazwa","'Zielona Sowa'"));
     //System.out.print(findShortestRelation("Wypozyczenie","id","2","Czytelnik","imie","'Wiktor'"));

        System.out.print(showAll());
    }
    private String showAll(){
        return graphDatabase.runCypher("MATCH (n) RETURN n LIMIT 25");
    }

    private String findBookByTitle(final String title) {

        return graphDatabase.runCypher("Match(ksiazka:Ksiazka{tytul:"+'"'+title+'"'+"}) " +
                "return ksiazka");
    }

    private String findReaderByName(final String name,final String lastName) {

        return graphDatabase.runCypher("Match(czytelnik:Czytelnik{imie:"+'"'+name+'"'+",nazwisko:"+'"'+lastName+'"'+"}) " +
                "return czytelnik");
    }

    private String findHireByID (final int id){
        return graphDatabase.runCypher("Match(wypozyczenie:Wypozyczenie{id:"+id+"}) " +
                "return wypozyczenie");
    }

    private void generator(){
            String[] imiona = {"Marcin", "Maria", "Anna", "Małgorzata", "Andrzej", "Agnieszka", "Paulina", "Jagoda", "Izabela", "Robert"};
            String[] nazwiska={"Nowak","Nowak","Wiśniewski","Dąbrowski","Lewandowski","Wójcik"};
            String[] tytuly={"Antygona","Balladyna","Wesele","Król Edyp","Kordian","Hamlet"};
        String[] miasta={"Chrzanów","Kraków","Warszawa"};
        Random generator = new Random();
            for(int i=0;i<5;i++) {
                String imie = imiona[generator.nextInt(imiona.length)];
                String nazwisko = nazwiska[generator.nextInt(nazwiska.length)];
                String miasto = miasta[generator.nextInt(miasta.length)];
//System.out.println("Tworze czytelnika"+imie+nazwisko);
                graphDatabase.runCypher("CREATE (czytelnik:Czytelnik {imie:" + "'" + imie + "'" + "," +
                        "nazwisko:" + "'" + nazwisko + "'" +
                        ",adres:" + "'" + miasto + "'" + "})");

                String tytul = tytuly[generator.nextInt(tytuly.length)];
                String imie1 = imiona[generator.nextInt(imiona.length)];
                String nazwisko1 = nazwiska[generator.nextInt(nazwiska.length)];
//System.out.println("Tworze ksiazke "+tytul);
                graphDatabase.runCypher("CREATE (ksiazka:Ksiazka {tytul:" + "'" + tytul + "'" + ",autor:" + "'" + imie1 + " " + nazwisko1 + "'" + "})");

                Date date=new Date(generator.nextInt());
                String[] statusy={"O","W"};
                String status=statusy[generator.nextInt(statusy.length)];
                String sdata=date.getDay()+"."+date.getMonth()+"."+date.getYear();

                String s=getID();
                String s1=s.substring(65,75);
                String[] parts=s1.split(" ");
                Integer id=Integer.parseInt(parts[0]);
                id++;

                graphDatabase.runCypher("Create (wypozyczenie:Wypozyczenie {id:"+id+",data:"+ "'" +sdata+ "'" +",status:"+ "'" +status+ "'" +"})");

                graphDatabase.runCypher("Match (czytelnik:Czytelnik),(wypozyczenie:Wypozyczenie) where czytelnik.imie="+ "'" +imie+ "'" +" and czytelnik.nazwisko="+ "'" +nazwisko+ "'" +"  and wypozyczenie.id="+id+" Create (wypozyczenie)-[r:Kto]->(czytelnik)");
                graphDatabase.runCypher("Match (ksiazka:Ksiazka),(wypozycznie:Wypozyczenie)where ksiazka.tytul="+ "'" +tytul+ "'" +" and wypozycznie.id="+id+" Create (wypozycznie)-[r:Jaka]->(ksiazka)");
            }
        }

    private String findRelationBy(final String first,final String typ1,final String value1){

        return graphDatabase.runCypher("Match(tmp:"+first+"{"+typ1+":"+value1+"})-[r]->(b)\n" +
                "return r,tmp,b");
    }

    private String findShortestRelation(final String first,final String typ1,final String value1,final String secound , final String type2 ,
                                        final String value2 ){
        return graphDatabase.runCypher("Match p=shortestPath((n1:"+first+"{"+typ1+":"+value1+"})-[*..6]->(n2:"+secound
                +"{"+type2+":"+value2+"}))\n" +
                "return p");


    }

    private String getID(){
        return graphDatabase.runCypher("Match(wypozyczenia:Wypozyczenie) return wypozyczenia.id order by wypozyczenia.id DESC limit 1");
    }



}






