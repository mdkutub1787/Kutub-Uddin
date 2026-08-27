import 'dart:math' as math;

import 'package:flutter/material.dart';
import 'package:flutter_compass/flutter_compass.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:deen_life/core/localization/app_localizations.dart';

import '../providers/qibla_provider.dart';

class QiblaScreen extends ConsumerWidget {
  const QiblaScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final qiblaData = ref.watch(qiblaProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text(context.tr('Qibla Compass')),
        centerTitle: true,
      ),
      body: StreamBuilder<CompassEvent>(
        stream: FlutterCompass.events,
        builder: (context, snapshot) {
          if (qiblaData.status == QiblaStatus.error) {
            return Center(
              child: Text('${context.tr('Error')}: ${qiblaData.errorMsg}'),
            );
          }

          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          double? heading = snapshot.data?.heading;

          if (heading == null) {
            return Center(
              child: Text(context.tr('Device does not have compass sensors.')),
            );
          }

          final double qiblaDirection = qiblaData.qiblaDirection;

          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  '${heading.toStringAsFixed(0)}°',
                  style: const TextStyle(
                    fontSize: 48,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(height: 10),
                Text(
                  context.tr('Qibla'),
                  style: const TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    color: Colors.green,
                  ),
                ),
                const SizedBox(height: 20),
                Stack(
                  alignment: Alignment.center,
                  children: [
                    // The compass dial (points North)
                    Transform.rotate(
                      angle: (heading * (math.pi / 180) * -1),
                      child: Container(
                        width: 300,
                        height: 300,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          border: Border.all(
                            color: Theme.of(context).colorScheme.primary
                                .withOpacity(0.5),
                            width: 2,
                          ),
                        ),
                        child: const Stack(
                          alignment: Alignment.center,
                          children: [
                            Positioned(
                              top: 10,
                              child: Text(
                                'N',
                                style: TextStyle(
                                  fontSize: 24,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.red,
                                ),
                              ),
                            ),
                            Positioned(
                              bottom: 10,
                              child: Text(
                                'S',
                                style: TextStyle(
                                  fontSize: 24,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            Positioned(
                              right: 10,
                              child: Text(
                                'E',
                                style: TextStyle(
                                  fontSize: 24,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            Positioned(
                              left: 10,
                              child: Text(
                                'W',
                                style: TextStyle(
                                  fontSize: 24,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),

                    // The Qibla pointer
                    Transform.rotate(
                      angle: ((qiblaDirection - heading) * (math.pi / 180)),
                      child: const Icon(
                        Icons.navigation,
                        size: 150,
                        color: Colors.green, // Kaaba color representation
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 50),
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 32.0),
                  child: Text(
                    context.tr(
                      'Rotate your phone until the green arrow points to the top.',
                    ),
                    textAlign: TextAlign.center,
                    style: const TextStyle(color: Colors.grey, fontSize: 16),
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}

