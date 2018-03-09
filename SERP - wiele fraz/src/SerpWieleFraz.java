import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author Created  by Michal Nowak
 **/
public class SerpWieleFraz {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textDomena;
    @FXML
    private Button buttonSprawdz;
    @FXML
    private Label labelPozycja;

    @FXML
    public void Sprawdz() {
        String s=textArea.getText();
        String[] words=s.split("\n");
        SprawdzPozycje(words,textDomena.getText());


        //SprawdzPozycje(textFraza.getText(), textDomena.getText());
    }

    public void SprawdzPozycje(String[] fraza, String domena) {
        Runnable task = new Runnable() { // tworzenie watku
            @Override
            public void run() {
                try {
                    File file = new File("src/Frazy.txt");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);      // otworzenie
                    // strumienia przesyłającego znaki w postaci bajtowej
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream); // tłumacz pliku na
                    // postać bajtową
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter); // obiekt odpowiedzialny
                    // za wysyłanie znaków
                    for(String frazaPojedyncza:fraza) {


                        System.setProperty("webdriver.chrome.driver", "C:\\Users\\nowak\\Selenium\\chromedriver.exe"); // ustawienie miejsca dodaku do przegladarki chrome
                        WebDriver driver = new ChromeDriver(); // tworzenie obiektu przegldarki
                        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);// czeka 15 sekund, jesli storna sie nie zaladuje to odswieza
                        driver.manage().window().setPosition(new Point(-2000, 0)); // przeglądarka bdzie schowana
                        driver.get("https://google.pl/");  // wejscie na strone google.pl
                        Thread.sleep(2000); // 2 sekundy oczekiwania
                        String source = driver.getPageSource(); // pobranie kodu zrodlowego strony
                        String pozycja = "Brak poączenia z internetem";
                        if (source.contains("ERR_INTERNET_DISCONNECTED")) { // sprawdzenie czy kod zrodlowy strony zawiera informacje o braku polaczneia z internetem
                            labelPozycja.setText("Brak polaczenia z internetem");
                            driver.close(); // zakmniecie przeglądarki
                            driver.quit();  // wylaczenie przegladarki

                        } else {
                            driver.get("https://cmonitor.pl/sprawdz-pozycje");// wejscie na strone cmonitor.pl
                            //driver.navigate().refresh();
                            driver.findElement(By.xpath("//*[@id='phrases']")).sendKeys(frazaPojedyncza); // wpisanie frazy
                            driver.findElement(By.xpath("//*[@id='domain']")).sendKeys(domena); // wpisanie nazwy domeny
                            driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div[2]/div[1]/div/form/div[2]/a")).click(); // klikniecie przyciksku sprawdz pozycje
                            Thread.sleep(5300); // 5,3 sekund oczekiwania
                            pozycja = driver.findElement(By.xpath("//*[@id='result_0']")).getText(); // pobranie pozycji
                            driver.close(); // zakmniecie przeglądarki
                            driver.quit();   // wylaczenie przegladarki

                            /////////////////////////////////////////////////////////////////

                            try {
                                bufferedWriter.write(frazaPojedyncza + " - " + pozycja + "\n"); //zapis danych do pliku


                            } catch (IOException ex) {
                                System.out.print("Wystąpil blad zapisu");
                            }
                        }
                    }
                    bufferedWriter.close();
                    Scanner scanner = new Scanner(file);    // utworzenie obiektu typu Scanner i przkazanie w konstruktorze pliku
                    String s="";                            // wstępna inicjalizacja pola typu String
                    while(scanner.hasNextLine()){           // zapis fraz z pozycjmi do pliku
                        s = s +scanner.nextLine()+"\n";
                    }
                    textArea.setText(s); // wypisanie fraz z pozycjami
                    /////////////////////////////////////////////////////////////////
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {  // watek odpowiedzialny za komunikacje z GUI
                            labelPozycja.setText("Sprawdzono");
                            buttonSprawdz.setDisable(false);

                        }
                    });
                } catch (Exception e) {

                }
            }
        };
        try {
            new Thread(task).start(); // rozpoczecie dzialania watku
            labelPozycja.setText("Sprawdzanie...");
            buttonSprawdz.setDisable(true);

        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }


    }


}
