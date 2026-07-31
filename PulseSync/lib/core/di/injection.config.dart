// GENERATED CODE - DO NOT MODIFY BY HAND
// dart format width=80

// **************************************************************************
// InjectableConfigGenerator
// **************************************************************************

// ignore_for_file: type=lint
// coverage:ignore-file

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:get_it/get_it.dart' as _i174;
import 'package:injectable/injectable.dart' as _i526;

import '../../features/inventory/data/repositories/inventory_repository_impl.dart'
    as _i572;
import '../../features/inventory/domain/repositories/inventory_repository.dart'
    as _i422;
import '../../features/inventory/domain/usecases/search_inventory.dart'
    as _i869;
import '../../features/inventory/presentation/bloc/inventory_bloc.dart'
    as _i690;

extension GetItInjectableX on _i174.GetIt {
  // initializes the registration of main-scope dependencies inside of GetIt
  _i174.GetIt init({
    String? environment,
    _i526.EnvironmentFilter? environmentFilter,
  }) {
    final gh = _i526.GetItHelper(this, environment, environmentFilter);
    gh.lazySingleton<_i422.InventoryRepository>(
      () => _i572.InventoryRepositoryImpl(),
    );
    gh.factory<_i869.SearchInventoryUseCase>(
      () => _i869.SearchInventoryUseCase(gh<_i422.InventoryRepository>()),
    );
    gh.factory<_i690.InventoryBloc>(
      () => _i690.InventoryBloc(gh<_i869.SearchInventoryUseCase>()),
    );
    return this;
  }
}
