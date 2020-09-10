# Code Coverage
Two kinds of Tests contribute to the code coverage
1. Unit tests (ie. all tests ending with `*Test.java`)
2. Integration tests (ie. All tests ending with `*IT.java`)

Jacoco framework generates the data file after execution of tests and this data file is used by Jacoco Reports `jacoco:report` as well as SonarQube Service

Jacoco Plugin has been configured on `pom.xml` to generate different Coverage Data file for Unit tests as well as Integration tests.

1. Add new properties within `<properties>` tag
   * The `jacoco-it.exec` file is for Integration tests
   * The `jacoco-ut.exec` file is for Unit tests
    ```xml
    <aggregate.report.dir>${project.build.directory}/coverage-reports/jacoco-aggregate/jacoco.xml</aggregate.report.dir>
    <jacoco.it.execution.data.file>${project.build.directory}/coverage-reports/jacoco-it.exec</jacoco.it.execution.data.file>
    <jacoco.ut.execution.data.file>${project.build.directory}/coverage-reports/jacoco-ut.exec</jacoco.ut.execution.data.file>
    ```
2. Add/update the following Jacoco Build Plugin
   ```xml
   <plugin>
      <groupId>org.jacoco</groupId>
      <artifactId>jacoco-maven-plugin</artifactId>
      <version>0.8.5</version>
      <executions>
        <execution>
          <id>jacoco-pre-unit-test</id>
          <goals>
            <goal>prepare-agent</goal>
          </goals>
          <configuration>
            <!-- Sets the path to the file which contains the execution data. -->
            <destFile>${project.build.directory}/coverage-reports/jacoco-ut.exec</destFile>
            <propertyName>surefireArgLine</propertyName>
            <excludes>
              <exclude>**/*IT.class</exclude>
              <exclude>**/dto/*.class</exclude>
              <exclude>**/bo/**/*</exclude>
              <exclude>**/vo/**/*</exclude>
              <exclude>**/configuration/**/*</exclude>
              <exclude>**/config/**/*</exclude>
              <exclude>**/configs/**/*</exclude>
              <exclude>**/exceptions/**/*</exclude>
            </excludes>
          </configuration>
        </execution>
        <execution>
          <id>jacoco-post-unit-test</id>
          <phase>test</phase>
          <goals>
            <goal>report</goal>
          </goals>
          <configuration>
            <title>Unit Tests Coverage Report</title>
            <excludes>
              <exclude>**/*IT.class</exclude>
              <exclude>**/dto/*.class</exclude>
              <exclude>**/bo/**/*</exclude>
              <exclude>**/vo/**/*</exclude>
              <exclude>**/configuration/**/*</exclude>
              <exclude>**/config/**/*</exclude>
              <exclude>**/configs/**/*</exclude>
              <exclude>**/exceptions/**/*</exclude>
            </excludes>
            <!-- Sets the path to the file which contains the execution data. -->
            <dataFile>${jacoco.ut.execution.data.file}</dataFile>
            <!-- Sets the output directory for the code coverage report. -->
            <outputDirectory>${project.reporting.outputDirectory}/jacoco-ut</outputDirectory>
          </configuration>
        </execution>
        <execution>
          <id>jacoco-pre-integration-test</id>
          <phase>pre-integration-test</phase>
          <goals>
            <goal>prepare-agent-integration</goal>
          </goals>
          <configuration>
            <!-- Sets the path to the file which contains the execution data. -->
            <destFile>${jacoco.it.execution.data.file}</destFile>
            <append>true</append>
            <!-- Sets the name for JaCoCo runtime agent settings property -->
            <propertyName>failsafeArgLine</propertyName>
            <excludes>
              <exclude>**/dto/*.class</exclude>
              <exclude>**/bo/**/*</exclude>
              <exclude>**/vo/**/*</exclude>
              <exclude>**/configuration/**/*</exclude>
              <exclude>**/config/**/*</exclude>
              <exclude>**/configs/**/*</exclude>
              <exclude>**/exceptions/**/*</exclude>
            </excludes>
          </configuration>
        </execution>
        <execution>
          <id>jacoco-post-integration-test</id>
          <phase>post-integration-test</phase>
          <goals>
            <goal>report</goal>
          </goals>
          <configuration>
            <title>Integration Tests Coverage Report</title>
            <!-- Sets the path to the file which contains the execution data. -->
            <dataFile>${jacoco.it.execution.data.file}</dataFile>
            <!-- Sets the output directory for the code coverage report. -->
            <outputDirectory>${project.reporting.outputDirectory}/jacoco-it</outputDirectory>
            <excludes>
              <exclude>**/dto/*.class</exclude>
              <exclude>**/bo/**/*</exclude>
              <exclude>**/vo/**/*</exclude>
              <exclude>**/configuration/**/*</exclude>
              <exclude>**/config/**/*</exclude>
              <exclude>**/configs/**/*</exclude>
              <exclude>**/exceptions/**/*</exclude>
            </excludes>
          </configuration>
        </execution>
        <execution>
          <id>jacoco-merge-unit-and-integration</id>
          <phase>post-integration-test</phase>
          <goals>
            <goal>merge</goal>
          </goals>
          <configuration>
            <fileSets>
              <fileSet>
                <directory>${project.build.directory}/coverage-reports/</directory>
                <includes>
                  <include>*.exec</include>
                </includes>
              </fileSet>
            </fileSets>
            <destFile>${project.build.directory}/coverage-reports/merged.exec</destFile>
          </configuration>
        </execution>
        <execution>
          <id>jacoco-create-merged-report</id>
          <phase>post-integration-test</phase>
          <goals>
            <goal>report</goal>
          </goals>
          <configuration>
            <title>Merged Test Coverage Report</title>
            <dataFile>${project.build.directory}/coverage-reports/merged.exec</dataFile>
            <outputDirectory>${project.reporting.outputDirectory}/full-coverage-report</outputDirectory>
            <excludes>
              <exclude>**/dto/*.class</exclude>
              <exclude>**/bo/**/*</exclude>
              <exclude>**/vo/**/*</exclude>
              <exclude>**/configuration/**/*</exclude>
              <exclude>**/config/**/*</exclude>
              <exclude>**/configs/**/*</exclude>
              <exclude>**/exceptions/**/*</exclude>
            </excludes>
          </configuration>
        </execution>
      </executions>
   </plugin>
   ``` 
   The above plugin configuration does the following:
   1. Unit Tests coverage
      1. Configure output data file for Unit test coverage with exclusions
      2. Configures the Unit test coverage report with exclusions
      3. Export the Jacoco configuration of Unit tests via `surefireArgLine`
   2. Integration Test coverage
      1. Configure output data file for Integration Test coverage with exclusions
      2. Configures the Integration Test coverage report with exclusions
      3. Export the Jacoco configuration of Integration test via `failsafeArgLine`
   3. Merge Unit tests and Integration tests
      1. Configures the coverage data files used for merging, as defined by the output location 
      2. Configure a merged converage report with exclusions

3. Exclusions/Ignoring some classes/packages from code coverage (Optional)<br>
Often it is required to ignore certain classes from the code coverage reports and such classes or packages can be added to the `excludes` section
    ```xml
    <excludes>
      <exclude>**/dto/*.class</exclude>
      <exclude>**/bo/*.class</exclude>
      <exclude>**/vo/*.class</exclude>
      <exclude>**/configuration/*.class</exclude>
      <exclude>**/config/*.class</exclude>
      <exclude>**/configs/*.class</exclude>
    </excludes>
    ```
4. Finally, add/update the Surefire(Unit tests) and Failsafe(Integration Tests) plugins
   ```xml
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId> <!-- surefire plugin version managed by Spring Boot -->
      <version>3.0.0-M4-SNAPSHOT</version>
      <configuration>
        <skipTests>false</skipTests>
        <argLine>${surefireArgLine}</argLine>
        <properties>
          <configurationParameters>
            junit.jupiter.conditions.deactivate = *
            junit.jupiter.extensions.autodetection.enabled = true
            junit.jupiter.testinstance.lifecycle.default = per_class
            junit.jupiter.execution.parallel.enabled = true
          </configurationParameters>
        </properties>
      </configuration>
    </plugin>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-failsafe-plugin</artifactId>
      <configuration>
        <argLine>${failsafeArgLine}</argLine>
      </configuration>
      <executions>
        <execution>
          <id>integration-tests</id>
          <goals>
            <goal>integration-test</goal>
            <goal>verify</goal>
          </goals>
        </execution>
      </executions>
   </plugin>
   ```
   * The above configuration instructs the SureFire plugin to use the Unit Test Jacoco data file via `sureFireArgLine`
   * The above configuration instructs the Failsafe plugin to use the Integration Test Jacoco data file via `failsafeArgLine`
