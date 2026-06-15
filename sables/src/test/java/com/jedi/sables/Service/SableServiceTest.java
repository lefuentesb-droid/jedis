package com.jedi.sables.Service;

// Importamos métodos de JUnit para validar los resultados de las pruebas
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Importamos métodos de Mockito para simular y verificar llamadas
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Importamos Optional porque findById devuelve un Optional
import java.util.Optional;

// Importamos List para poder simular listas de sables
import java.util.List;

// Importamos anotaciones de JUnit
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Importamos anotaciones de Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

// Importamos nuestras clases del proyecto sables
import com.jedi.sables.Dto.SableDTO;
import com.jedi.sables.Repository.SableRepository;
import com.jedi.sables.model.Sable;

// Importamos Faker para generar datos falsos
import net.datafaker.Faker;

// Esta anotación permite que JUnit trabaje junto con Mockito
@ExtendWith(MockitoExtension.class)
class SableServiceTest {

    // @Mock crea una versión falsa del repositorio.
    // Esto significa que NO se conectará a la base de datos real.
    // Solo simulará respuestas para probar el Service.
    @Mock
    private SableRepository sableRepository;

    // @InjectMocks crea una instancia real de SableService.
    // Además, le inyecta el sableRepository falso que creamos arriba.
    // Así probamos la lógica de SableService sin depender de MySQL.
    @InjectMocks
    private SableService sableService;

    // Faker sirve para crear datos de prueba.
    // En este caso lo usamos para inventar valores de color o cristal.
    private Faker faker = new Faker();

    // Este método se ejecuta antes de cada test.
    // Sirve para inicializar correctamente los mocks de Mockito.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------
    // PRUEBA 1: Buscar un sable por ID cuando sí existe
    // -------------------------------------------------------------
    @Test
    void testBuscarPorId_Exitoso() {

        // GIVEN / DADO:
        // Creamos un ID falso que usaremos para buscar el sable.
        Integer idSimulado = 1;

        // Creamos datos falsos para el sable.
        String colorSimulado = "Azul";
        String cristalSimulado = "Ilum";
        Integer jediIdSimulado = 2;

        // Creamos un objeto Sable falso.
        // Este objeto representa lo que normalmente vendría desde la base de datos.
        Sable sableFalso = new Sable();
        sableFalso.setId(idSimulado);
        sableFalso.setColor(colorSimulado);
        sableFalso.setCristalKyber(cristalSimulado);
        sableFalso.setJediId(jediIdSimulado);

        // Le decimos al repositorio falso:
        // "Cuando alguien busque por este ID, devuelve este sable falso".
        when(sableRepository.findById(idSimulado)).thenReturn(Optional.of(sableFalso));

        // WHEN / CUANDO:
        // Ejecutamos el método real del Service que queremos probar.
        SableDTO resultado = sableService.buscarPorId(idSimulado);

        // THEN / ENTONCES:
        // Validamos que el resultado no sea nulo.
        assertNotNull(resultado);

        // Validamos que el ID del DTO sea igual al ID simulado.
        assertEquals(idSimulado, resultado.getId());

        // Validamos que el color del DTO sea igual al color del sable falso.
        assertEquals(colorSimulado, resultado.getColor());

        // Validamos que el cristal kyber se haya convertido correctamente.
        assertEquals(cristalSimulado, resultado.getCristalKyber());

        // Validamos que el jediId sea el esperado.
        assertEquals(jediIdSimulado, resultado.getJediId());

        // Verificamos que findById se haya ejecutado exactamente una vez.
        verify(sableRepository, times(1)).findById(idSimulado);
    }

    // -------------------------------------------------------------
    // PRUEBA 2: Buscar un sable por ID cuando NO existe
    // -------------------------------------------------------------
    @Test
    void testBuscarPorId_NoEncontrado() {

        // GIVEN / DADO:
        // Creamos un ID que simularemos como inexistente.
        Integer idSimulado = 99;

        // Le decimos al repositorio falso:
        // "Cuando busquen este ID, devuelve vacío".
        // Optional.empty() significa que no encontró nada.
        when(sableRepository.findById(idSimulado)).thenReturn(Optional.empty());

        // WHEN / THEN:
        // Validamos que al buscar un sable inexistente,
        // el método lance una RuntimeException.
        assertThrows(RuntimeException.class, () -> {
            sableService.buscarPorId(idSimulado);
        });

        // Verificamos que findById se haya llamado exactamente una vez.
        verify(sableRepository, times(1)).findById(idSimulado);
    }

    // -------------------------------------------------------------
    // PRUEBA 3: Guardar un sable correctamente
    // -------------------------------------------------------------
    @Test
    void testGuardar_Exitoso() {

        // GIVEN / DADO:
        // Creamos un sable nuevo.
        // Este simula el objeto que llegaría desde un POST.
        // No tiene ID porque el ID normalmente lo genera la base de datos.
        Sable nuevoSable = new Sable();
        nuevoSable.setColor("Morado");
        nuevoSable.setCristalKyber("Adegan");
        nuevoSable.setJediId(2);

        // Creamos otro sable que representa el resultado después de guardar.
        // Este sí tiene ID porque simula que la base de datos ya lo guardó.
        Sable sableGuardado = new Sable();
        sableGuardado.setId(3);
        sableGuardado.setColor("Morado");
        sableGuardado.setCristalKyber("Adegan");
        sableGuardado.setJediId(2);

        // Le decimos al repositorio falso:
        // "Cuando guardes nuevoSable, devuelve sableGuardado".
        when(sableRepository.save(nuevoSable)).thenReturn(sableGuardado);

        // WHEN / CUANDO:
        // Ejecutamos el método guardar del Service.
        SableDTO resultado = sableService.guardar(nuevoSable);

        // THEN / ENTONCES:
        // Validamos que el resultado no sea nulo.
        assertNotNull(resultado);

        // Validamos que el ID sea el que tiene el sable guardado.
        assertEquals(3, resultado.getId());

        // Validamos que el color sea correcto.
        assertEquals("Morado", resultado.getColor());

        // Validamos que el cristal kyber sea correcto.
        assertEquals("Adegan", resultado.getCristalKyber());

        // Validamos que el jediId sea correcto.
        assertEquals(2, resultado.getJediId());

        // Verificamos que save se haya ejecutado exactamente una vez.
        verify(sableRepository, times(1)).save(nuevoSable);
    }

    // -------------------------------------------------------------
    // PRUEBA 4 EXTRA: Obtener todos los sables cuando hay datos
    // -------------------------------------------------------------
    // La guía pide 3 pruebas para Sable, pero esta cuarta prueba deja el trabajo más sólido.
    // Si tu profe pide mínimo 3, tener 4 no te perjudica.
    @Test
    void testObtenerTodos_Exitoso() {

        // GIVEN / DADO:
        // Creamos el primer sable falso.
        Sable sable1 = new Sable();
        sable1.setId(1);
        sable1.setColor("Verde");
        sable1.setCristalKyber("Ilum");
        sable1.setJediId(1);

        // Creamos el segundo sable falso.
        Sable sable2 = new Sable();
        sable2.setId(2);
        sable2.setColor("Azul");
        sable2.setCristalKyber("Ilum");
        sable2.setJediId(2);

        // Le decimos al repositorio falso:
        // "Cuando llamen a findAll, devuelve esta lista de sables".
        when(sableRepository.findAll()).thenReturn(List.of(sable1, sable2));

        // WHEN / CUANDO:
        // Ejecutamos el método obtenerTodos del Service.
        List<SableDTO> resultado = sableService.obtenerTodos();

        // THEN / ENTONCES:
        // Validamos que la lista no sea nula.
        assertNotNull(resultado);

        // Validamos que la lista tenga 2 elementos.
        assertEquals(2, resultado.size());

        // Validamos que el primer sable tenga color Verde.
        assertEquals("Verde", resultado.get(0).getColor());

        // Validamos que el segundo sable tenga color Azul.
        assertEquals("Azul", resultado.get(1).getColor());

        // Verificamos que findAll se haya ejecutado exactamente una vez.
        verify(sableRepository, times(1)).findAll();
    }
}