package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = IndividualClient.tableIndividualClient)
@PrimaryKeyJoinColumn(name = IndividualClient.individualClientpersonId)
internal class IndividualClient(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = individualClientclientId, referencedColumnName = Client.clientId)
    var client: Client,
    name: String,
    surname: String,
    pseudoPESEL: String
) : Person(name, surname, pseudoPESEL) {
    companion object {
        const val tableIndividualClient = "IndividualClient"
        const val individualClientpersonId = "personId"
        const val individualClientclientId = "clientId"
    }
}