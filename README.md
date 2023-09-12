# Notes App
Aplicativo de anotação bem simples, usuário consegue adicionar as suas anotações é deletar, ambas são salvas em um banco local, ou seja, após abrir e fechar ira manter os dados


## Feature
![arquitetura](./arquitetura.png)
##

- Pode [usar isto para encontrar as bibliotecas que tem suporte](https://developer.android.com/jetpack/compose/libraries?hl=pt-br)
- Para salvar os dados no banco Squilete local usamos o Room e arquitetura desenhada acima
- Usamos injeção dependência,View Model,Flow e Coretines
- Primeiro crio um arquivo que ira iniciar o Application  e defino no AndroidManifest.xml a tag application com o nome do projeto
- Depois preciso indicia o ponto de partida para injeção dependência

```kotlin

//AndroidManifest.xml
//android:name
 <application
        android:name=".NotesApplication"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        
//Arquivo Aplication
@HiltAndroidApp
class NotesApplication:  Application() {}

//MainActive
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    NotesScreen()
                }
            }
        }
    }
}


```

##
- Após isto preciso criar o modelo é a classe do banco de dados
- Model seria as entidades da tabela, caso deseja salvar um dado na tabela com nome diferente do que esta no modelo pode usar a propriedade ColumnInfo
- Preciso de uma primary key, pois e um banco relacional
- Database exige o caminho das entidades que seria os arquivos em Model, sua versão caso precisa atualiza o banco de dados e só altera este numero e ultimo parâmetro é opcional se desejo exportar o esquema
- TypeCoverters sao as classes que usamos para converter os dados que o banco não consegue automaticamente inferir
- Dão e a classe que ira diretamente manipular o banco de dados
- Para essa classe usamos conceito do Coretines que significa assíncrono, aqui usamos    a palavra suspend, repara que no get eu não aplico, pois  defino que o retorno será um Flow, para dados assíncronos com Coretines o Flow encaixa melhor
- Import do Fflow precisa ser o import kotlinx.coroutines.flow.Flow
- Query anotação acima das funções são as tradicionais do banco de dados que usamos em Squilet, existe a opção no update é insert para definir se iremos sobrescrever o dado, caso estamos tentando manipular um valor existente, com a propriedade OnConflictStrategy.REPLACE


```kotlin

//NotesModel
@Entity(tableName = "notes")
//se não colocar o nome da entity considerara nomne da classe
data class NotesModel(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var title: String,
    var description: String,
    var entryDate: Date = Date()
)


//NotesDatabase
@Database(entities = [NotesModel::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class,UuidConverter::class)
abstract class NotesDatabase: RoomDatabase() {
    abstract  fun NotesDao(): NotesDao
}

//Dao
@Dao
interface NotesDao {
  
    @Query("SELECT * from notes")
    fun getAllNotes(): Flow<List<NotesModel>>

    @Query("SELECT * from notes where id =:id")
    suspend fun getSingleNote(id: String): NotesModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(notesModel: NotesModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(notesModel: NotesModel)


    @Query("DELETE  from notes")
    suspend fun deleteAllNotes()


    @Delete
    suspend fun deleteSingleNote(notesModel: NotesModel)

}


```
##
- Depois de tudo isso precisamos de um módulo para injeção dependência é ele que ira prover os acessos
- Eu provenho o acesso ao Dão e o Database, não preciso instanciar essa classe em nenhum lugar, pois será gerada automaticamente quando fizer o rebuild do projeto

```kotlin

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

   
    @Singleton
    @Provides
    fun notesDatabaseDao(notesDatabase: NotesDatabase): NotesDao = notesDatabase.NotesDao()

 
   
    @Singleton
    @Provides
    fun notesDatabase(@ApplicationContext context: Context): NotesDatabase = Room.databaseBuilder(
        context,
        NotesDatabase::class.java,
        "notes_db"
    )
        .fallbackToDestructiveMigration()
        .build()

}

```

## 
- Agora está tudo pronto para iniciarmos as injeções dependências, primeiro começo na camada Repository, depois View Model
- No View Model se não tivesse usando Coretines posso simplesmente criar funções normais e aplicar na View , agora por estar usando assíncrono preciso do viewModelScope.launch
- Também preciso determinar que nossos dados e do tipo MutableStateFlow já que desde do início definimos que o retorno do banco seria um Flow
- Normalmente se não for assíncrono usaremos anotação mutableState que um listener  
- Na árvore única mudança que vamos fazer e dizer que os dados será collectAsState() para recuperar o último dado disponível no Flow

```kotlin
// Repository

class NotesRepository @Inject constructor(private val notesDatabase: NotesDao) {

    suspend fun add(notesModel: NotesModel) = notesDatabase.addNote(notesModel)
    suspend fun update(notesModel: NotesModel) = notesDatabase.updateNote(notesModel)
    suspend fun deleteOnly(notesModel: NotesModel) = notesDatabase.deleteSingleNote(notesModel)
    suspend fun deleteAll() = notesDatabase.deleteAllNotes()
    //conflate sempre ira pegar o valor atulizado do suspense
    suspend fun get(): Flow<List<NotesModel>> = notesDatabase.getAllNotes().flowOn(Dispatchers.IO).conflate()


}


//View Model
@HiltViewModel
class NotesViewModel @Inject constructor(private val repositoryNote: NotesRepository) :
    ViewModel() {
     // se não estiver usando flow ou dados assincronos o mutableState e perfeito
    // private var listNotes = mutableStateListOf<NotesModel>() 
    //estou usando stateflow porque o tipo que retorna no room que determinei e um flow
    private val _noteList = MutableStateFlow<List<NotesModel>>(emptyList())
    val noteList = _noteList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            //distincUntilChanged ira retornar o flow
            //collect retorna nosso flow que no caso e a list de de notes
            repositoryNote.get().distinctUntilChanged().collect { listOfNotes ->
                if (listOfNotes.isEmpty()) {
                    Log.d("ListOfNodes", "List is empty")
                } else {
                    _noteList.value = listOfNotes
                }
            }
        }
    }

   
    fun addNote(notesModel: NotesModel) = viewModelScope.launch {
        repositoryNote.add(notesModel)
    }

    fun updateNote(notesModel: NotesModel) = viewModelScope.launch {
        repositoryNote.update(notesModel)
    }

    fun deleteNote(notesModel: NotesModel) = viewModelScope.launch {
        repositoryNote.deleteOnly(notesModel)
    }


}


//Arvore
fun NotesScreen(notesViewModel: NotesViewModel = viewModel()) {
val listNotes = notesViewModel.noteList.collectAsState().value

....
}

```


##
- Quando estamos lidando com data ou uuid precisamos converter esses dados e definir la no Database, abaixo exemplo de como converter data
  
```kotlin
//Date Converter
class  DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return   Date(timestamp)
    }

    @TypeConverter
    fun longTimeStamp(date: Date): Long {
        return  date.time
    }
}

// UuidConverter
class UuidConverter {

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun stringToUUID(uuid: String): UUID {
        return UUID.fromString(uuid)
    }

}



```
