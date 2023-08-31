package pl.macia.printinghouse.server.test.repo

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.test.InMemProfile
@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@ContextConfiguration(classes = [InMemProfile::class])
internal class EmailRepoTest {

}