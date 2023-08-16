package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

const val tableIndividualClient = "IndividualClient"
const val individualClientpersonId = "personId"
const val individualClientclientId = "clientId"

@Poko
@Entity
@Table(name = tableIndividualClient)
@PrimaryKeyJoinColumn(name = individualClientpersonId)
class IndividualClient(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = individualClientclientId, referencedColumnName = clientId)
    var client: Client,
    name: String,
    surname: String,
    pseudoPESEL: String
) : Person(name, surname, pseudoPESEL)