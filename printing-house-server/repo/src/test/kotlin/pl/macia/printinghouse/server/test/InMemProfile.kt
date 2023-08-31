package pl.macia.printinghouse.server.test

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@PropertySource("classpath:inMemDB.properties")
@EnableTransactionManagement
class InMemProfile