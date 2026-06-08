import 'package:fflipy/core/widgets/preloader.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/widgets/brand_app_bar.dart';

class SearchScreen extends ConsumerStatefulWidget {
  const SearchScreen({super.key});

  @override
  ConsumerState<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends ConsumerState<SearchScreen>
    with SingleTickerProviderStateMixin {
  late TextEditingController _searchController;
  bool _showFilters = false;
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _searchController = TextEditingController();
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  void _performSearch(String query) async {
    if (query.isEmpty) {
      setState(() {});
      return;
    }
    setState(() => _isLoading = true);
    await Future.delayed(const Duration(seconds: 1));
    if (mounted) {
      setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: BrandAppBar(
        title: TextField(
          controller: _searchController,
          autofocus: true,
          style: const TextStyle(color: Colors.white),
          decoration: const InputDecoration(
            hintText: 'Search...',
            hintStyle: TextStyle(color: Colors.white70),
            border: InputBorder.none,
          ),
          onChanged: _performSearch,
        ),
        actions: [
          IconButton(
            icon: Icon(
                _showFilters ? Icons.filter_list_off : Icons.filter_list),
            onPressed: () {
              setState(() {
                _showFilters = !_showFilters;
              });
            },
          ),
        ],
      ),
      body: Stack(
        children: [
          Column(
            children: [
              if (_showFilters)
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: const Row(
                    children: [
                      Text('Filter Options Here'),
                    ],
                  ),
                ),
              Expanded(
                child: Center(
                  child: Text(
                    'Search Results for "${_searchController.text}"',
                    style: const TextStyle(fontSize: 18),
                  ),
                ),
              ),
            ],
          ),
          if (_isLoading)
            const Preloader(),
        ],
      ),
    );
  }
}
