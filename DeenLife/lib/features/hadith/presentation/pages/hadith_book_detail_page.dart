import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/online_hadith_provider.dart';
import '../../../../core/data/sihah_sittah_data.dart';
import 'hadith_section_page.dart';

class HadithBookDetailPage extends ConsumerStatefulWidget {
  final HadithBook book;

  const HadithBookDetailPage({super.key, required this.book});

  @override
  ConsumerState<HadithBookDetailPage> createState() => _HadithBookDetailPageState();
}

class _HadithBookDetailPageState extends ConsumerState<HadithBookDetailPage> {
  String _searchQuery = '';
  final TextEditingController _searchController = TextEditingController();

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final bookDataAsync = ref.watch(onlineHadithProvider(widget.book.id));

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.book.nameBn),
        centerTitle: true,
      ),
      body: bookDataAsync.when(
        data: (data) {
          final sections = data['metadata']?['sections'] as Map<String, dynamic>? ?? {};
          final hadiths = data['hadiths'] as List<dynamic>? ?? [];

          // If searching, filter hadiths and show them directly
          if (_searchQuery.isNotEmpty) {
            final searchResults = hadiths.where((h) {
              final textBn = (h['text'] ?? '').toString().toLowerCase();
              return textBn.contains(_searchQuery.toLowerCase());
            }).toList();

            return _buildSearchResults(searchResults);
          }

          // Otherwise show Chapters
          final sectionKeys = sections.keys.toList()..sort((a, b) => int.parse(a).compareTo(int.parse(b)));

          return Column(
            children: [
              _buildSearchBar(),
              Expanded(
                child: RefreshIndicator(
                  onRefresh: () async {
                    await Future.delayed(const Duration(milliseconds: 1000));
                  },
                  child: ListView.builder(
                    padding: const EdgeInsets.all(16.0),
                  itemCount: sectionKeys.length,
                  itemBuilder: (context, index) {
                    final key = sectionKeys[index];
                    final sectionName = sections[key].toString();
                    
                    // Skip empty section names if they exist (usually '0' is empty)
                    if (sectionName.isEmpty && key == '0') return const SizedBox();

                    return Card(
                      margin: const EdgeInsets.only(bottom: 12),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      child: ListTile(
                        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                        leading: CircleAvatar(
                          backgroundColor: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                          child: Text(
                            key,
                            style: TextStyle(
                              color: Theme.of(context).colorScheme.primary,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                        title: Text(
                          sectionName.isEmpty ? 'Chapter $key' : sectionName,
                          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                        ),
                        trailing: const Icon(Icons.arrow_forward_ios, size: 16),
                        onTap: () {
                          // Filter hadiths by this section
                          final sectionHadiths = hadiths.where((h) {
                            return h['reference']?['book'].toString() == key;
                          }).toList();

                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => HadithSectionPage(
                                bookName: widget.book.nameEn,
                                sectionName: sectionName.isEmpty ? 'Chapter $key' : sectionName,
                                sectionHadiths: sectionHadiths,
                              ),
                            ),
                          );
                        },
                      ),
                    );
                  },
                ),
              ),
            ),
          ],
        );
      },
        loading: () => Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const CircularProgressIndicator(),
              const SizedBox(height: 16),
              Text(
                'Downloading ${widget.book.nameBn}...\nThis may take a few moments.\nOnce downloaded, it will be available offline forever!',
                textAlign: TextAlign.center,
                style: const TextStyle(height: 1.5),
              ),
            ],
          ),
        ),
        error: (err, stack) => Center(
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              'Error downloading ${widget.book.nameBn}.\nPlease check your internet connection.\n$err',
              textAlign: TextAlign.center,
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildSearchBar() {
    return Container(
      padding: const EdgeInsets.all(16),
      color: Theme.of(context).scaffoldBackgroundColor,
      child: TextField(
        controller: _searchController,
        decoration: InputDecoration(
          hintText: 'Search in this book...',
          prefixIcon: const Icon(Icons.search),
          suffixIcon: _searchQuery.isNotEmpty
              ? IconButton(
                  icon: const Icon(Icons.clear),
                  onPressed: () {
                    _searchController.clear();
                    setState(() {
                      _searchQuery = '';
                    });
                  },
                )
              : null,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(30),
            borderSide: BorderSide.none,
          ),
          filled: true,
          fillColor: Colors.grey[200],
        ),
        onChanged: (value) {
          setState(() {
            _searchQuery = value;
          });
        },
      ),
    );
  }

  Widget _buildSearchResults(List<dynamic> searchResults) {
    return Column(
      children: [
        _buildSearchBar(),
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16.0),
          child: Align(
            alignment: Alignment.centerLeft,
            child: Text(
              'Found ${searchResults.length} results',
              style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.grey),
            ),
          ),
        ),
        const SizedBox(height: 8),
        Expanded(
          child: RefreshIndicator(
            onRefresh: () async {
              await Future.delayed(const Duration(milliseconds: 1000));
            },
            child: ListView.builder(
              padding: const EdgeInsets.all(16.0),
            itemCount: searchResults.length,
            itemBuilder: (context, index) {
              final h = searchResults[index];
              return Card(
                margin: const EdgeInsets.only(bottom: 16),
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Hadith ${h['hadithnumber'] ?? ''}',
                        style: TextStyle(
                          color: Theme.of(context).colorScheme.primary,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        h['text'] ?? '',
                        style: const TextStyle(fontSize: 16, height: 1.5),
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ),
    ],
  );
  }
}
