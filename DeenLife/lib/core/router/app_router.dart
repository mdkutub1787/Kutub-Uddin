import 'package:go_router/go_router.dart';

// Import all screens
import '../../features/splash/presentation/screens/splash_screen.dart';
import '../../shared/widgets/main_layout.dart';
import '../../features/home/presentation/screens/home_screen.dart';
import '../../features/explore/presentation/screens/explore_screen.dart';
import '../../features/calendar/presentation/screens/calendar_screen.dart';
import '../../features/quran/presentation/screens/quran_screen.dart';
import '../../features/settings/presentation/screens/settings_screen.dart';
import '../../features/qibla/presentation/screens/qibla_screen.dart';
import '../../features/masjid/presentation/screens/masjid_list_screen.dart';
import '../../features/masjid/presentation/screens/set_masjid_times_screen.dart';
import '../../features/notifications/presentation/screens/notification_screen.dart';
import '../../features/diary/presentation/screens/diary_screen.dart';
import '../../features/calendar/presentation/screens/calendar_grid_screen.dart';
import '../../features/calendar/presentation/screens/permanent_prayer_times_screen.dart';
import '../../features/emotions/presentation/screens/emotions_screen.dart';
import '../../features/hadith/presentation/screens/hadith_screen.dart';
import '../../features/hadith/presentation/screens/hadith_book_detail_screen.dart';
import '../../features/hadith/presentation/screens/hadith_section_screen.dart';
import '../../features/quiz/presentation/screens/quiz_screen.dart';
import '../../features/quran/presentation/screens/quran_search_screen.dart';
import '../../features/quran/presentation/screens/surah_detail_screen.dart';
import '../../features/quran/presentation/screens/tafsir_screen.dart';
import '../../features/quran/presentation/screens/tafsir_surah_list_screen.dart';
import '../../features/learning/presentation/screens/knowledge_hub_screen.dart';
import '../../features/learning/presentation/screens/namaz_shikkha_screen.dart';
import '../../features/learning/presentation/screens/book_reading_screen.dart';
import '../../features/learning/presentation/screens/four_imams_screen.dart';
import '../../features/learning/presentation/screens/guide_detail_screen.dart';
import '../../features/learning/presentation/screens/quran_learning_screen.dart';
import '../../features/kalima/presentation/screens/kalima_screen.dart';
import '../../features/radio/presentation/screens/radio_screen.dart';
import '../../features/zakat/presentation/screens/zakat_calculator_screen.dart';
import '../../features/duas/presentation/screens/dua_screen.dart';
import '../../features/asmaul_husna/presentation/screens/asmaul_husna_screen.dart';
import '../../features/tasbeeh/presentation/screens/tasbeeh_screen.dart';

final GoRouter appRouter = GoRouter(
  initialLocation: '/splash',
  routes: [
    GoRoute(
      path: '/splash',
      builder: (context, state) => const SplashScreen(),
    ),
    GoRoute(
      path: '/main',
      builder: (context, state) => const MainLayout(),
    ),
    GoRoute(
      path: '/home',
      builder: (context, state) => const HomeScreen(),
    ),
    GoRoute(
      path: '/explore',
      builder: (context, state) => const ExploreScreen(),
    ),
    GoRoute(
      path: '/quran',
      builder: (context, state) => const QuranScreen(),
    ),
    GoRoute(
      path: '/quran_search',
      builder: (context, state) => const QuranSearchScreen(),
    ),
    GoRoute(
      path: '/surah_detail',
      builder: (context, state) {
        final args = state.extra as Map<String, dynamic>;
        return SurahDetailScreen(
          surahNumber: args['surahNumber'] as int,
          surahName: args['surahName'] as String,
        );
      },
    ),
    GoRoute(
      path: '/tafsir',
      builder: (context, state) => const TafsirScreen(),
    ),
    GoRoute(
      path: '/tafsir_surah_list',
      builder: (context, state) {
        final args = state.extra as Map<String, dynamic>;
        return TafsirSurahListScreen(
          tafsirId: args['tafsirId'] as int,
          tafsirName: args['tafsirName'] as String,
        );
      },
    ),
    GoRoute(
      path: '/calendar',
      builder: (context, state) => const CalendarScreen(),
    ),
    GoRoute(
      path: '/calendar_grid',
      builder: (context, state) => const CalendarGridScreen(),
    ),
    GoRoute(
      path: '/permanent_prayer',
      builder: (context, state) => const PermanentPrayerTimesScreen(),
    ),
    GoRoute(
      path: '/settings',
      builder: (context, state) => const SettingsScreen(),
    ),
    GoRoute(
      path: '/qibla',
      builder: (context, state) => const QiblaScreen(),
    ),
    GoRoute(
      path: '/masjid_list',
      builder: (context, state) => const MasjidListScreen(),
    ),
    GoRoute(
      path: '/set_masjid_times',
      builder: (context, state) => const SetMasjidTimesScreen(),
    ),
    GoRoute(
      path: '/notifications',
      builder: (context, state) => const NotificationScreen(),
    ),
    GoRoute(
      path: '/diary',
      builder: (context, state) => const DiaryScreen(),
    ),
    GoRoute(
      path: '/emotions',
      builder: (context, state) => const EmotionsScreen(),
    ),
    GoRoute(
      path: '/hadith',
      builder: (context, state) => const HadithScreen(),
    ),
    GoRoute(
      path: '/hadith_book',
      builder: (context, state) => HadithBookDetailScreen(book: state.extra as dynamic),
    ),
    GoRoute(
      path: '/hadith_section',
      builder: (context, state) {
        final args = state.extra as Map<String, dynamic>;
        return HadithSectionScreen(
          bookName: args['bookName'] as String,
          sectionName: args['sectionName'] as String,
          sectionHadiths: args['sectionHadiths'] as List<dynamic>,
        );
      },
    ),
    GoRoute(
      path: '/quiz',
      builder: (context, state) => const QuizScreen(),
    ),
    GoRoute(
      path: '/knowledge_hub',
      builder: (context, state) => const KnowledgeHubScreen(),
    ),
    GoRoute(
      path: '/namaz_shikkha',
      builder: (context, state) => const NamazShikkhaScreen(),
    ),
    GoRoute(
      path: '/book_reading',
      builder: (context, state) => BookReadingScreen(book: state.extra as dynamic),
    ),
    GoRoute(
      path: '/four_imams',
      builder: (context, state) => const FourImamsScreen(),
    ),
    GoRoute(
      path: '/guide_detail',
      builder: (context, state) => GuideDetailScreen(topic: state.extra as dynamic),
    ),
    GoRoute(
      path: '/quran_learning',
      builder: (context, state) => const QuranLearningScreen(),
    ),
    GoRoute(
      path: '/kalima',
      builder: (context, state) => KalimaScreen(),
    ),
    GoRoute(
      path: '/radio',
      builder: (context, state) => const RadioScreen(),
    ),
    GoRoute(
      path: '/zakat',
      builder: (context, state) => const ZakatCalculatorScreen(),
    ),
    GoRoute(
      path: '/dua',
      builder: (context, state) => DuaScreen(),
    ),
    GoRoute(
      path: '/asmaul_husna',
      builder: (context, state) => const AsmaulHusnaScreen(),
    ),
    GoRoute(
      path: '/tasbeeh',
      builder: (context, state) => const TasbeehScreen(),
    ),
  ],
);
