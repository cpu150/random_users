package com.example.cpu150.randomusers

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.cpu150.randomusers.models.GetRandomUsersModel
import com.example.cpu150.randomusers.models.RandomUserModel
import com.example.cpu150.randomusers.models.RandomUserNameModel
import com.example.cpu150.randomusers.models.RandomUserPictureModel
import com.example.cpu150.randomusers.network.RandomUserEndpoints
import com.example.cpu150.randomusers.viewmodels.HomePageCardViewModel
import com.example.cpu150.randomusers.viewmodels.HomePageViewModel
import io.reactivex.Single.just
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import java.net.URI
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.plugins.RxAndroidPlugins
import org.junit.Assert.*
import io.reactivex.plugins.RxJavaPlugins
import org.junit.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.junit.Rule
import com.google.gson.GsonBuilder
import java.io.InputStreamReader

class HomePageViewModelTest {
    @get:Rule val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock private lateinit var randomUserEndpoints: RandomUserEndpoints

    @InjectMocks lateinit var homePageViewModel: HomePageViewModel

    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            // Set all RxJava threads on the current thread (so all requests are synchronous)
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

            // In order to test LiveData, the `InstantTaskExecutorRule` rule needs to be applied via JUnit.
            // However as we are running it with MockitoJUnit, the "rule" will be implemented in this way instead
            ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) {
                    runnable.run()
                }

                override fun isMainThread(): Boolean {
                    return true
                }

                override fun postToMainThread(runnable: Runnable) {
                    runnable.run()
                }
            })
        }
    }

    private var defaultGetRandomUsers: GetRandomUsersModel

    init {
        // Load JSON file
        val `in` = javaClass.classLoader?.getResourceAsStream("GetRandomUsers.json")

        defaultGetRandomUsers = if (`in` != null)
            GsonBuilder().create().fromJson<GetRandomUsersModel>(InputStreamReader(`in`), GetRandomUsersModel::class.java)
        else
            GetRandomUsersModel(null)
    }

    private val defaultEmail = "nicolas.poncet1@gmail.com"
    private val defaultLargeUrl = "https://randomuser.me/api/portraits/men/74.jpg"
    private val defaultMediumUrl = "https://randomuser.me/api/portraits/med/men/74.jpg"
    private val defaultThumbnailUrl = "https://randomuser.me/api/portraits/thumb/men/74.jpg"
    private val defaultUrl = "https://randomuser.me/api/portraits/default/img.jpg"
    private val defaultPictureModel = RandomUserPictureModel (URI.create(defaultLargeUrl), URI.create(defaultMediumUrl), URI.create(defaultThumbnailUrl))
    private val defaultNameModel = RandomUserNameModel("Mr", "First", "Last")

    private fun requestRandomUsers(listOfUsers: List<RandomUserModel>) {

        `when`(randomUserEndpoints.getRandomUsers(listOfUsers.size))
            .thenReturn(just(GetRandomUsersModel(listOfUsers)))

        homePageViewModel.requestData(listOfUsers.size)

        assertNotNull(homePageViewModel.dataListLiveData.value)
        assertNotNull(homePageViewModel.dataListLiveData.value?.size)
        assertEquals(homePageViewModel.dataListLiveData.value?.size, listOfUsers.size)
    }

    private val longNameFormat = "%s. %s %s"
    private val mediumTitleNameFormat = "%s. %s"
    private val mediumNameFormat = "%s %s"
    private val shortNameFormat = "%s"
    private val defaultName = "No name"

    private fun getName (homePageCardViewModel: HomePageCardViewModel?): String? {
        return homePageCardViewModel?.getName(longNameFormat, mediumTitleNameFormat, mediumNameFormat, shortNameFormat, defaultName)
    }

    @Test
    fun testJsonFile() {
        assertNotNull(defaultGetRandomUsers.results)
        assertEquals(defaultGetRandomUsers.results?.size, 10)

        defaultGetRandomUsers.results?.also {
            requestRandomUsers (it)
        }

        val list = homePageViewModel.dataListLiveData.value
        assertEquals (getName (list?.get(0)), "madame. marine da silva")
        assertEquals (list?.get(0)?.getAvatarUrlString(null), "https://randomuser.me/api/portraits/women/60.jpg")
    }

    @Test
    fun testValidUser() {
        requestRandomUsers (listOf(RandomUserModel (defaultNameModel, defaultEmail, defaultPictureModel)))
    }

    @Test
    fun usersNameRules() {

        requestRandomUsers (listOf(
            RandomUserModel (RandomUserNameModel("Mr", "First", "Last"), defaultEmail, defaultPictureModel), // 0
            RandomUserModel (RandomUserNameModel("Mr", "First", null), defaultEmail, defaultPictureModel), // 1
            RandomUserModel (RandomUserNameModel("Mr", null, "Last"), defaultEmail, defaultPictureModel), // 2
            RandomUserModel (RandomUserNameModel("Mr", null, null), defaultEmail, defaultPictureModel), // 3
            RandomUserModel (RandomUserNameModel(null, "First", "Last"), defaultEmail, defaultPictureModel), // 4
            RandomUserModel (RandomUserNameModel(null, "First", null), defaultEmail, defaultPictureModel), // 5
            RandomUserModel (RandomUserNameModel(null, null, "Last"), defaultEmail, defaultPictureModel), // 6
            RandomUserModel (RandomUserNameModel(null, null, null), defaultEmail, defaultPictureModel))) // 7

        val list = homePageViewModel.dataListLiveData.value
        assertEquals (getName (list?.get(0)), "Mr. First Last")
        assertEquals (getName (list?.get(1)), "First")
        assertEquals (getName (list?.get(2)), "Mr. Last")
        assertEquals (getName (list?.get(3)), defaultName)
        assertEquals (getName (list?.get(4)), "First Last")
        assertEquals (getName (list?.get(5)), "First")
        assertEquals (getName (list?.get(6)), "Last")
        assertEquals (getName (list?.get(7)), defaultName)
    }

    @Test
    fun pictureUrlRules() {

        requestRandomUsers (listOf(
            RandomUserModel (defaultNameModel, defaultEmail, RandomUserPictureModel (URI.create(defaultLargeUrl), URI.create(defaultMediumUrl), URI.create(defaultThumbnailUrl))), // 0
            RandomUserModel (defaultNameModel, defaultEmail, RandomUserPictureModel (URI.create(defaultLargeUrl), URI.create(defaultMediumUrl), null)), // 1
            RandomUserModel (defaultNameModel, defaultEmail, RandomUserPictureModel (URI.create(defaultLargeUrl), null, URI.create(defaultThumbnailUrl))), // 2
            RandomUserModel (defaultNameModel, defaultEmail, RandomUserPictureModel (URI.create(defaultLargeUrl), null, null)), // 3
            RandomUserModel (defaultNameModel, defaultEmail, RandomUserPictureModel (null, URI.create(defaultMediumUrl), URI.create(defaultThumbnailUrl))), // 4
            RandomUserModel (defaultNameModel, defaultEmail, RandomUserPictureModel (null, URI.create(defaultMediumUrl), null)), // 5
            RandomUserModel (defaultNameModel, defaultEmail, RandomUserPictureModel (null, null, URI.create(defaultThumbnailUrl))), // 6
            RandomUserModel (defaultNameModel, defaultEmail, RandomUserPictureModel (null, null, null)))) // 7

        val list = homePageViewModel.dataListLiveData.value
        assertEquals (list?.get(0)?.getAvatarUrlString(defaultUrl), defaultLargeUrl)
        assertEquals (list?.get(1)?.getAvatarUrlString(defaultUrl), defaultLargeUrl)
        assertEquals (list?.get(2)?.getAvatarUrlString(defaultUrl), defaultLargeUrl)
        assertEquals (list?.get(3)?.getAvatarUrlString(defaultUrl), defaultLargeUrl)
        assertEquals (list?.get(4)?.getAvatarUrlString(defaultUrl), defaultMediumUrl)
        assertEquals (list?.get(5)?.getAvatarUrlString(defaultUrl), defaultMediumUrl)
        assertEquals (list?.get(6)?.getAvatarUrlString(defaultUrl), defaultThumbnailUrl)
        assertEquals (list?.get(7)?.getAvatarUrlString(defaultUrl), defaultUrl)
    }
}