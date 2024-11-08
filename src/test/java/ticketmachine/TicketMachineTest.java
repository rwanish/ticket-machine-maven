package ticketmachine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TicketMachineTest {
	private static final int PRICE = 50; // Une constante

	private TicketMachine machine; // l'objet à tester

	@BeforeEach
	public void setUp() {
		machine = new TicketMachine(PRICE); // On initialise l'objet à tester
	}

	@Test
	// On vérifie que le prix affiché correspond au paramètre passé lors de
	// l'initialisation
	// S1 : le prix affiché correspond à l’initialisation.
	void priceIsCorrectlyInitialized() {
		// Paramètres : valeur attendue, valeur effective, message si erreur
		assertEquals(PRICE, machine.getPrice(), "Initialisation incorrecte du prix");
	}

	@Test
	// S2 : la balance change quand on insère de l’argent
	void insertMoneyChangesBalance() {
		machine.insertMoney(10);
		machine.insertMoney(20);
		// Les montants ont été correctement additionnés
		assertEquals(10 + 20, machine.getBalance(), "La balance n'est pas correctement mise à jour");
	}

	@Test
	// S3 : N'imprime pas si pas assez d'argent
	void nImprimePasBalanceInsuffisante(){
		//GIVEN : une machine vierge (initialisée dans @BeforeEach)
		//WHEN : On ne met pas assez d'argent
		machine.insertMoney(PRICE - 1);
		//THEN ça n'imprime pas
		assertFalse(machine.printTicket(), "Pas assez d'argent, on doit imprimer");
	}

	@Test
		// S4 : N'imprime pas si pas assez d'argent
	void imprimeSiBalanceSuffisante(){
		//GIVEN : une machine vierge (initialisée dans @BeforeEach)
		//WHEN : On ne met pas assez d'argent
		machine.insertMoney(PRICE);
		//THEN ça imprime
		assertTrue(machine.printTicket(), "Il y a assez d'argent, on doit imprimer");
	}

	@Test
		// S5 : Quand on imprime un ticket la balance est décrémentée du prix du ticket
	void printTicketDecrementsBalance() {
		machine.insertMoney(PRICE);
		machine.printTicket();
		assertEquals(0, machine.getBalance(), "La balance doit être décrémentée après impression du ticket");
	}


	// S6 : Le montant collecté est mis à jour quand on imprime un ticket (pas avant)
	@Test
	void printTicketUpdatesTotalOnlyAfterPrinting() {
		int initialTotal = machine.getTotal();
		machine.insertMoney(PRICE);
		assertEquals(initialTotal, machine.getTotal(), "Le montant collecté ne doit pas changer avant impression du ticket");
		machine.printTicket();
		assertEquals(initialTotal + PRICE, machine.getTotal(), "Le montant collecté doit être mis à jour après impression du ticket");
	}

	// S7 : refund() rend correctement la monnaie
	@Test
	void refundReturnsCorrectBalance() {
		machine.insertMoney(30);
		machine.insertMoney(20);
		assertEquals(50, machine.refund(), "La monnaie rendue doit être égale à la balance");
	}

	// S8 : refund() remet la balance à zéro
	@Test
	void refundResetsBalance() {
		machine.insertMoney(PRICE);
		machine.refund();
		assertEquals(0, machine.getBalance(), "La balance doit être remise à zéro après remboursement");
	}

	// S9 : On ne peut pas insérer un montant négatif
	@Test
	void cannotInsertNegativeAmount() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			machine.insertMoney(-10);
		});
		assertEquals("Montant négatif non autorisé", exception.getMessage(), "Un montant négatif ne doit pas être accepté");
	}

	// S10 : On ne peut pas créer de machine qui délivre des tickets dont le prix est négatif
	@Test
	void cannotCreateMachineWithNegativePrice() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			new TicketMachine(-1);
		});
		assertEquals("Le prix du ticket ne peut pas être négatif", exception.getMessage(), "Le prix du ticket ne doit pas être négatif");
	}

}
