package pl.xpu.external.verificator.vat

import java.time.LocalDate

class GovConnector {

    private static String getFormattedDate(){
        LocalDate date = LocalDate.now()
        return date.format( 'yyyy-MM-dd')
    }

    public static String getResponse(String nip){
        String date = getFormattedDate() //2024-05-02
        String requestedURL = "https://wl-api.mf.gov.pl/api/search/nip/${nip}?date=${date}"
        URL postmanGet = new URL(requestedURL)
        return postmanGet.getText()
    }
}
