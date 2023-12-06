package com.reflexian.staff;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class StaffPluginLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder) {
        MavenLibraryResolver mysql = new MavenLibraryResolver();
        mysql.addRepository(new RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build());
        mysql.addDependency(new Dependency(new DefaultArtifact("mysql:mysql-connector-java:8.0.28"), null));

        MavenLibraryResolver inventoryapi = new MavenLibraryResolver();
        inventoryapi.addRepository(new RemoteRepository.Builder("jitpack", "default", "https://jitpack.io").build());
        inventoryapi.addDependency(new Dependency(new DefaultArtifact("com.github.MinusKube:SmartInvs:9c9dbbee16"), null));

        pluginClasspathBuilder.addLibrary(inventoryapi);
        pluginClasspathBuilder.addLibrary(mysql);
    }

}
