import 'dart:math' as math;
import 'package:flutter/material.dart';
import 'package:flutter_compass/flutter_compass.dart';

class QiblaPage extends StatelessWidget {
  const QiblaPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Qibla Compass'),
        centerTitle: true,
      ),
      body: StreamBuilder<CompassEvent>(
        stream: FlutterCompass.events,
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return Center(child: Text('Error reading compass: ${snapshot.error}'));
          }

          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          double? heading = snapshot.data?.heading;

          if (heading == null) {
            return const Center(child: Text('Device does not have compass sensors.'));
          }

          // Qibla direction from Bangladesh is roughly 261 degrees (West-South-West)
          // We can calculate this dynamically using geolocator in the future
          const double qiblaDirection = 261.0; 
          
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
                const SizedBox(height: 50),
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
                            color: Theme.of(context).colorScheme.primary.withOpacity(0.5),
                            width: 2,
                          ),
                        ),
                        child: const Stack(
                          alignment: Alignment.center,
                          children: [
                            Positioned(
                              top: 10,
                              child: Text('N', style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold, color: Colors.red)),
                            ),
                            Positioned(
                              bottom: 10,
                              child: Text('S', style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
                            ),
                            Positioned(
                              right: 10,
                              child: Text('E', style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
                            ),
                            Positioned(
                              left: 10,
                              child: Text('W', style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
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
                const Padding(
                  padding: EdgeInsets.symmetric(horizontal: 32.0),
                  child: Text(
                    'Rotate your phone until the green arrow points to the top.',
                    textAlign: TextAlign.center,
                    style: TextStyle(color: Colors.grey, fontSize: 16),
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
