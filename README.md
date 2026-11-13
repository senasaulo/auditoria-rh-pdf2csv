# Cruzamento de Relatórios de Funcionários

Aplicação Java para comparar dois relatórios de funcionários em PDF e gerar um CSV com os registros que permaneceram, foram demitidos ou foram contratados.

## Requisitos

- JDK 25
- Apache Maven

O projeto usa Apache PDFBox 2.0.30 para extrair texto dos PDFs.

## Compilar e executar

Na pasta do projeto, execute:

```powershell
mvn clean package
mvn exec:java -Dexec.mainClass=Main
```

O programa pede primeiro o PDF antigo e depois o PDF atual. Os seletores iniciam na pasta `Arquivos`. Ao final, escolha o nome do CSV; a pasta inicial para salvar é `Cruzamento_Dados`.

As pastas são localizadas a partir da raiz do projeto e criadas automaticamente quando necessário.

## Relatorios de entrada

Use PDFs com texto selecionável, não apenas imagens digitalizadas. O processamento espera relatórios com matrícula numérica de três dígitos no início das linhas dos funcionários e campos de nome, local e cargo reconhecíveis no texto extraído. Linhas de cabeçalho e outros fragmentos que não representem registros de funcionários são ignorados na comparação.

## Configuracao

Os arquivos de configuração ficam em `Arquivos` e aceitam um termo por linha:

- `linhas_indesejadas.txt`: prefixos de linhas a remover do processamento.
- `cargos.txt`: cargos reconhecidos para separar o campo de cargo. O tratamento de `TERAPEUTA` também evita dividir `FISIOTERAPEUTA`.

Se um arquivo estiver ausente, o programa o cria novamente com os termos padrão.

## Resultado

O CSV usa ponto e vírgula como separador e inclui as colunas:

`SITUACAO;MATRICULA;LOCAL;NOME;CARGO;CATEGORIA;HOR;NF;ND;DT_ADM;SIN;OPT;SALARIO`

Os valores de situação são `permanencia`, `demitidos` e `contratados`. O cruzamento usa matrícula e nome.

## PDFs ficticios para teste

Em `Arquivos` há dois relatórios inteiramente fictícios, sem dados de funcionários reais:

- `Relatorio_Ficticio_Antigo.pdf`
- `Relatorio_Ficticio_Atual.pdf`

Cada um contém 800 registros. Entre as versões, 80 registros foram substituídos, resultando em 720 permanências, 80 demissões e 80 contratações no teste.

## Privacidade

Os PDFs de teste são fictícios. Não publique relatórios, arquivos CSV ou outros documentos que contenham dados pessoais ou salariais reais de funcionários.
