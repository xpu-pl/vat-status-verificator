package pl.xpu.external.verificator.vat

import groovy.json.JsonException
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.mop.ConfineMetaClassChanges

class VatVerificatorTest extends Specification {

    @ConfineMetaClassChanges(URL)
    def "verify returns true for active statusVat"() {
        given:
        URL.metaClass.getText = { mockOutput() }

        expect:
        VatVerificator.verify("7792488801") == true
    }

    @Unroll
    def "verify returns false for inactive statusVat"() {
        given:
        URL.metaClass.getText = { mockOutput("Cokolwiek innego niż: Czynny") }

        expect:
        VatVerificator.verify("7792488801") == false
    }

    @Unroll
    def "verify returns IllegalArgumentException if provided nip is incorrect"() {
        given:
        URL.metaClass.getText = { mockOutput() }

        when:
        VatVerificator.verify(nip)
        then:
        thrown(IllegalArgumentException)

        where:
        nip  << [null, "", "incorrect-nip"]
    }

    def "verify returns IOException if response from Gov webservice is incorrect"() {
        given:
        URL.metaClass.getText = { throw new IOException() }

        when:
        VatVerificator.verify("7792488801")
        then:
        thrown(IOException)
    }

    def "verify returns JsonException if response from Gov webservice is invalid"() {
        given:
        URL.metaClass.getText = { mockOutput('":":') }

        when:
        VatVerificator.verify("7792488801")
        then:
        thrown(JsonException)
    }

    private String mockOutput(String statusVat = "Czynny"){
        return """{"result":{"subject":{"name":"XPU SPÓŁKA Z OGRANICZONĄ ODPOWIEDZIALNOŚCIĄ","nip":"7792488801","statusVat":"${statusVat}","regon":"380077815","pesel":null,"krs":"0000730223","residenceAddress":null,"workingAddress":"AUGUSTYNA SZAMARZEWSKIEGO 26A/18, 60-517 POZNAŃ","representatives":[],"authorizedClerks":[],"partners":[],"registrationLegalDate":"2018-12-18","registrationDenialBasis":null,"registrationDenialDate":null,"restorationBasis":null,"restorationDate":null,"removalBasis":null,"removalDate":null,"accountNumbers":["62253000082058103631380001"],"hasVirtualAccounts":false},"requestId":"afnho-8n83gii","requestDateTime":"02-05-2024 19:05:06"}}"""
    }
}
