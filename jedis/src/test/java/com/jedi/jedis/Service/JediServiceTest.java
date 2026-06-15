package com.jedi.jedis.Service;

// Importamos métodos de JUnit para validar resultados en las pruebas
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Importamos métodos de Mockito para simular comportamientos
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Importamos estructuras necesarias de Java
import java.util.List;
import java.util.Optional;

// Importamos anotaciones de JUnit
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Importamos anotaciones de Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

// Importamos nuestras clases del proyecto
import com.jedi.jedis.Dto.JediDTO;
import com.jedi.jedis.Repository.JediRepository;
import com.jedi.jedis.model.Jedi;

// Importamos Faker para generar datos falsos de prueba
import net.datafaker.Faker;

// Esta anotación permite que JUnit trabaje con Mockito
@ExtendWith(MockitoExtension.class)
class JediServiceTest {

    // @Mock crea una versión falsa del Repository.
    // Esto significa que NO se conecta realmente a la base de datos.
    // Solo simula lo que debería responder el repository.
    @Mock//simula el repository.
    private JediRepository jediRepository;

    // @InjectMocks crea una instancia real de JediService,
    // pero le inyecta el jediRepository falso creado arriba.
    // Así probamos la lógica del Service sin depender de MySQL.
    @InjectMocks//mete ese repository falso dentro del service.
    private JediService jediService;

    // Faker sirve para crear datos falsos.
    // En este caso lo usamos para generar nombres estilo Star Wars.
    private Faker faker = new Faker();

    // Este método se ejecuta antes de cada prueba.
    // Sirve para inicializar correctamente los mocks de Mockito.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------
    // PRUEBA 1: Buscar un Jedi por ID cuando sí existe
    // -------------------------------------------------------------
    @Test
    void testBuscarPorId_Exitoso() {

        // GIVEN / DADO:
        // Preparamos los datos falsos que usaremos para la prueba.
        Integer idSimulado = 42;
        String nombreAleatorio = faker.starWars().character();

        // Creamos un objeto Jedi falso como si viniera desde la base de datos.
        Jedi jediFalso = new Jedi();
        jediFalso.setId(idSimulado);
        jediFalso.setNombre(nombreAleatorio);
        jediFalso.setMidiclorianos(faker.number().numberBetween(10000, 25000));
        jediFalso.setBando("Jedi");

        // Le decimos al repository falso:
        // "Cuando alguien busque por este ID, responde con este Jedi falso".
        when(jediRepository.findById(idSimulado)).thenReturn(Optional.of(jediFalso));//define qué debe responder el repository falso.

        // WHEN / CUANDO:
        // Ejecutamos el método real del Service que queremos probar.
        JediDTO resultado = jediService.buscarPorId(idSimulado);

        // THEN / ENTONCES:
        // Validamos que el resultado no venga nulo.
        assertNotNull(resultado);//revisan que el resultado sea correcto.

        // Validamos que el nombre del DTO sea el mismo que el del Jedi falso.
        assertEquals(nombreAleatorio, resultado.getNombre());//revisan que el resultado sea correcto.

        // Validamos que el bando también se haya convertido correctamente.
        assertEquals("Jedi", resultado.getBando());//revisan que el resultado sea correcto.

        // Verificamos que el método findById se haya llamado exactamente una vez.
        verify(jediRepository, times(1)).findById(idSimulado);//confirma que el service realmente llamó al repository.
    }

    // -------------------------------------------------------------
    // PRUEBA 2: Buscar un Jedi por ID cuando NO existe
    // -------------------------------------------------------------
    @Test
    void testBuscarPorId_NoEncontrado() {

        // GIVEN:
        // Usamos un ID simulado que no tendrá resultado.
        Integer idSimulado = 99;

        // Le decimos al repository falso:
        // "Cuando busquen este ID, responde vacío".
        when(jediRepository.findById(idSimulado)).thenReturn(Optional.empty());

        // WHEN / THEN:
        // Validamos que al buscar un ID inexistente se lance RuntimeException.
        assertThrows(RuntimeException.class, () -> {
            jediService.buscarPorId(idSimulado);
        });

        // Verificamos que findById haya sido llamado una sola vez.
        verify(jediRepository, times(1)).findById(idSimulado);
    }

    // -------------------------------------------------------------
    // PRUEBA 3: Obtener todos los Jedis cuando hay datos
    // -------------------------------------------------------------
    @Test
    void testObtenerTodos_Exitoso() {

        // GIVEN:
        // Creamos el primer Jedi falso.
        Jedi jedi1 = new Jedi();
        jedi1.setId(1);
        jedi1.setNombre("Grogu");
        jedi1.setMidiclorianos(15000);
        jedi1.setBando("Jedi");

        // Creamos el segundo Jedi falso.
        Jedi jedi2 = new Jedi();
        jedi2.setId(2);
        jedi2.setNombre("Luke Skywalker");
        jedi2.setMidiclorianos(20000);
        jedi2.setBando("Jedi");

        // Le decimos al repository falso:
        // "Cuando llamen a findAll, devuelve esta lista de Jedis".
        when(jediRepository.findAll()).thenReturn(List.of(jedi1, jedi2));

        // WHEN:
        // Ejecutamos el método real del Service.
        List<JediDTO> resultado = jediService.obtenerTodos();

        // THEN:
        // Validamos que la lista no sea nula.
        assertNotNull(resultado);

        // Validamos que la lista tenga 2 elementos.
        assertEquals(2, resultado.size());

        // Validamos que el primer nombre sea Grogu.
        assertEquals("Grogu", resultado.get(0).getNombre());

        // Verificamos que findAll se haya llamado una sola vez.
        verify(jediRepository, times(1)).findAll();//confirma que el service realmente llamó al repository.
    }

    // -------------------------------------------------------------
    // PRUEBA 4: Guardar un Jedi correctamente
    // -------------------------------------------------------------
    @Test
    void testGuardar_Exitoso() {

        // GIVEN:
        // Creamos un Jedi nuevo, como si viniera desde un POST.
        // Todavía no tiene ID porque se supone que la base de datos lo genera.
        Jedi nuevoJedi = new Jedi();
        nuevoJedi.setNombre("Ahsoka Tano");
        nuevoJedi.setMidiclorianos(17000);
        nuevoJedi.setBando("Jedi");

        // Creamos el Jedi que simula haber sido guardado en la base de datos.
        // Este sí tiene ID porque representa el resultado después de guardar.
        Jedi jediGuardado = new Jedi();
        jediGuardado.setId(3);
        jediGuardado.setNombre("Ahsoka Tano");
        jediGuardado.setMidiclorianos(17000);
        jediGuardado.setBando("Jedi");

        // Le decimos al repository falso:
        // "Cuando guarden este nuevoJedi, responde con jediGuardado".
        when(jediRepository.save(nuevoJedi)).thenReturn(jediGuardado);

        // WHEN:
        // Ejecutamos el método guardar del Service.
        JediDTO resultado = jediService.guardar(nuevoJedi);

        // THEN:
        // Validamos que el resultado no sea nulo.
        assertNotNull(resultado);//revisan que el resultado sea correcto.

        // Validamos que el ID sea el generado en el objeto guardado.
        assertEquals(3, resultado.getId());//revisan que el resultado sea correcto.

        // Validamos que el nombre se mantenga correctamente.
        assertEquals("Ahsoka Tano", resultado.getNombre());//revisan que el resultado sea correcto.

        // Validamos que el bando se mantenga correctamente.
        assertEquals("Jedi", resultado.getBando());//revisan que el resultado sea correcto.

        // Verificamos que save se haya ejecutado una sola vez.
        verify(jediRepository, times(1)).save(nuevoJedi);//confirma que el service realmente llamó al repository.
    }

    // -------------------------------------------------------------
    // PRUEBA 5: Obtener todos los Jedis cuando la lista está vacía
    // -------------------------------------------------------------
    @Test
    void testObtenerTodos_ListaVacia() {

        // GIVEN:
        // Le decimos al repository falso:
        // "Cuando llamen a findAll, devuelve una lista vacía".
        when(jediRepository.findAll()).thenReturn(List.of());

        // WHEN:
        // Ejecutamos el método obtenerTodos.
        List<JediDTO> resultado = jediService.obtenerTodos();

        // THEN:
        // Validamos que la lista no sea nula.
        assertNotNull(resultado);//revisan que el resultado sea correcto.

        // Validamos que la lista tenga tamaño 0.
        assertEquals(0, resultado.size());

        // Verificamos que findAll se haya llamado una sola vez.
        verify(jediRepository, times(1)).findAll();
    }
}