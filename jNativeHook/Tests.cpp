#include <Windows.h>
#include <string>
#include <iostream>

#define EXPORT comment(linker, "/EXPORT:" __FUNCTION__ "=" __FUNCDNAME__)


auto initialize() -> int {
    AllocConsole();
    FILE *fDummy;
    freopen_s(&fDummy, "CONIN$", "r", stdin);
    freopen_s(&fDummy, "CONOUT$", "w", stderr);
    freopen_s(&fDummy, "CONOUT$", "w", stdout);
    std::cout << "Running tests..." << std::endl;

	

    return 0;
}

extern "C" __declspec(dllexport) bool __stdcall RunTests()
{
    initialize();
	return true;
}