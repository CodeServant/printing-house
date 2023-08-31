package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = IndividualClient.TABLE_NAME)
@PrimaryKeyJoinColumn(name = IndividualClient.PERSON_ID)
internal class IndividualClient(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = CLIENT_ID, referencedColumnName = Client.ID)
    var client: Client,
    name: String,
    surname: String,
    pseudoPESEL: String
) : Person(name, surname, pseudoPESEL) {
    companion object {
        const val TABLE_NAME = "IndividualClient"
        const val PERSON_ID = "personId"
        const val CLIENT_ID = "clientId"
    }
}