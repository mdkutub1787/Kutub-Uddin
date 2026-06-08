import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'wave_clipper.dart';
import '../theme/app_theme.dart';

class BrandSliverAppBar extends StatelessWidget {
  final Widget? title;
  final List<Widget>? actions;
  final Widget? leading;
  final double expandedHeight;
  final bool pinned;
  final bool floating;
  final Widget? flexibleSpace;
  final bool centerTitle;

  const BrandSliverAppBar({
    super.key,
    this.title,
    this.actions,
    this.leading,
    this.expandedHeight = 200.0,
    this.pinned = true,
    this.floating = false,
    this.flexibleSpace,
    this.centerTitle = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return SliverAppBar(
      backgroundColor: theme.colorScheme.topBar,
      foregroundColor: Colors.black87,
      elevation: 0,
      systemOverlayStyle: const SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        statusBarIconBrightness: Brightness.dark,
      ),
      expandedHeight: expandedHeight,
      pinned: pinned,
      floating: floating,
      leading: leading,
      title: title,
      centerTitle: centerTitle,
      actions: actions,
      flexibleSpace: Stack(
        children: [
          ClipPath(
            clipper: WaveClipper(),
            child: Container(
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [
                    theme.colorScheme.topBarGradientLeft,
                    theme.colorScheme.topBarGradientRight,
                  ],
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                ),
              ),
            ),
          ),
          if (flexibleSpace != null) flexibleSpace!,
        ],
      ),
    );
  }
}
